package mg.grh.controller;

import jakarta.servlet.http.HttpSession;
import mg.grh.model.*;
import mg.grh.repository.*;
import mg.grh.service.CandidatService;
import mg.grh.service.CvService;
import mg.grh.service.FormationService;
import mg.grh.service.ExperienceProfessionnelleService;
import mg.grh.service.CompetenceService;
import mg.grh.web.SessionUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/** Parcours candidat en trois étapes, arrêté avant la soumission d'une candidature. */
@Controller
@RequestMapping({"/api/candidat", "/candidature"})
public class CandidatController {
    private final CandidatService candidatService;
    private final CvRepository cvRepository;
    private final CvService cvService;
    private final FormationService formationService;
    private final ExperienceProfessionnelleService experienceService;
    private final CompetenceService competenceService;
    private final FormationRepository formationRepository;
    private final ExperienceProfessionnelleRepository experienceRepository;
    private final CompetenceRepository competenceRepository;
    private final NiveauCompetenceRepository niveauRepository;
    private final Path uploadRoot;

    public CandidatController(CandidatService candidatService, CvRepository cvRepository, CvService cvService,
            FormationRepository formationRepository, ExperienceProfessionnelleRepository experienceRepository,
            CompetenceRepository competenceRepository, NiveauCompetenceRepository niveauRepository,
            FormationService formationService, ExperienceProfessionnelleService experienceService,
            CompetenceService competenceService,
            @Value("${grh.upload-dir:uploads/candidatures}") String uploadDir) {
        this.candidatService = candidatService;
        this.cvRepository = cvRepository;
        this.cvService = cvService;
        this.formationService = formationService;
        this.experienceService = experienceService;
        this.competenceService = competenceService;
        this.formationRepository = formationRepository;
        this.experienceRepository = experienceRepository;
        this.competenceRepository = competenceRepository;
        this.niveauRepository = niveauRepository;
        this.uploadRoot = Paths.get(uploadDir).toAbsolutePath().normalize();
    }

    @GetMapping({"", "/nouveau"})
    public String accueil(HttpSession session) {
        return estCandidat(session) ? "redirect:/api/candidat/etape/2" : "redirect:/api/candidat/etape/1";
    }

    @GetMapping("/etape/{etape}")
    public String afficherEtape(@PathVariable int etape, HttpSession session, Model model) {
        if (etape < 1 || etape > 3) return "redirect:/api/candidat/etape/1";
        SessionUser user = utilisateur(session);
        if (etape > 1 && (user == null || !"CANDIDAT".equals(user.getType())))
            return "redirect:/api/candidat/etape/1";
        model.addAttribute("etape", etape);
        if (estCandidat(session)) {
            Candidat candidat = candidatService.trouverParId(user.getId());
            model.addAttribute("candidat", candidat);
            Cv cv = cvRepository.findFirstByCandidatIdOrderByIdDesc(user.getId()).orElse(null);
            model.addAttribute("cv", cv);
            if (cv != null) {
                model.addAttribute("formations", formationRepository.findByCvId(cv.getId()));
                model.addAttribute("experiences", experienceRepository.findByCvId(cv.getId()));
                model.addAttribute("competences", competenceRepository.findByCvId(cv.getId()));
            }
            model.addAttribute("cvImporte", session.getAttribute("cvImporteCandidature"));
            model.addAttribute("lettre", session.getAttribute("lettreCandidature"));
            model.addAttribute("pieces", session.getAttribute("piecesCandidature"));
        }
        List<NiveauCompetence> niveaux = niveauRepository.findAll();
        if (niveaux.isEmpty()) {
            for (String libelle : List.of("Débutant", "Intermédiaire", "Avancé", "Expert")) {
                NiveauCompetence niveau = new NiveauCompetence();
                niveau.setLibelleNiveau(libelle);
                niveaux.add(niveauRepository.save(niveau));
            }
        }
        model.addAttribute("niveaux", niveaux);
        return "candidat/saisie-candidat";
    }

    @PostMapping("/etape/1")
    public String enregistrerCandidat(@RequestParam String nom, @RequestParam String prenom,
            @RequestParam String email, @RequestParam(required = false) String motDePasse,
            @RequestParam(required = false) LocalDate dtn, HttpSession session, RedirectAttributes ra) {
        try {
            SessionUser user = utilisateur(session);
            if (user != null && !"CANDIDAT".equals(user.getType()))
                throw new IllegalArgumentException("Seul un candidat peut créer ou modifier ce dossier.");
            Candidat candidat = user != null && "CANDIDAT".equals(user.getType())
                    ? candidatService.trouverParId(user.getId()) : new Candidat();
            candidat.setNomCandidat(nom);
            candidat.setPrenomCandidat(prenom);
            candidat.setEmail(email);
            candidat.setDtn(dtn);
            candidat = candidatService.enregistrerCandidat(candidat, motDePasse);
            if (user == null) session.setAttribute("user", new SessionUser("CANDIDAT", candidat.getId(), candidat.getEmail()));
            return "redirect:/api/candidat/etape/2";
        } catch (RuntimeException e) {
            ra.addFlashAttribute("erreur", message(e));
            ra.addFlashAttribute("nomSaisi", nom);
            ra.addFlashAttribute("prenomSaisi", prenom);
            ra.addFlashAttribute("emailSaisi", email);
            ra.addFlashAttribute("dtnSaisie", dtn);
            return "redirect:/api/candidat/etape/1";
        }
    }

    @PostMapping(value = "/etape/2", consumes = "multipart/form-data")
    @Transactional
    public String enregistrerCv(@RequestParam(required = false) String titreCv,
            @RequestParam(required = false) String profilProfessionnel, @RequestParam(required = false) String adresse,
            @RequestParam(required = false) String linkedinUrl, @RequestParam(required = false) String githubUrl,
            @RequestParam(required = false) List<String> formationNom,
            @RequestParam(required = false) List<String> formationEtablissement,
            @RequestParam(required = false) List<String> formationDebut,
            @RequestParam(required = false) List<String> formationFin,
            @RequestParam(required = false) List<String> experiencePoste,
            @RequestParam(required = false) List<String> experienceEntreprise,
            @RequestParam(required = false) List<String> experienceDebut,
            @RequestParam(required = false) List<String> experienceFin,
            @RequestParam(required = false) List<String> competenceNom,
            @RequestParam(required = false) List<Integer> competenceNiveau,
            @RequestParam(required = false) MultipartFile cvImporte,
            @RequestParam(required = false, defaultValue = "suivant") String direction,
            HttpSession session, RedirectAttributes ra) {
        try {
            Candidat candidat = candidatConnecte(session);
            if (cvImporte != null && !cvImporte.isEmpty()) {
                verifierFichier(cvImporte, Set.of("pdf", "docx"));
                session.setAttribute("cvImporteCandidature", enregistrerFichier(cvImporte, candidat.getId(), "cv"));
            }
            Cv cv = cvRepository.findFirstByCandidatIdOrderByIdDesc(candidat.getId()).orElseGet(Cv::new);
            cv.setCandidat(candidat);
            cv.setTitreCv(limiter(titreCv, 150));
            cv.setProfilProfessionnel(profilProfessionnel);
            cv.setAdresse(limiter(adresse, 255));
            cv.setLinkedinUrl(linkedinUrl);
            cv.setGithubUrl(githubUrl);
            if (cv.getDateCreation() == null) cv.setDateCreation(LocalDateTime.now());
            cv = cvService.enregistrer(cv);

            formationRepository.deleteByCvId(cv.getId());
            for (int i = 0; i < taille(formationNom); i++) {
                String nom = valeur(formationNom, i);
                if (nom == null) continue;
                Formation formation = new Formation();
                formation.setCv(cv);
                formation.setNomFormation(limiter(nom, 100));
                formation.setEtablissement(limiter(valeur(formationEtablissement, i), 200));
                formation.setDateDebut(date(valeur(formationDebut, i)));
                formation.setDateFin(date(valeur(formationFin, i)));
                formationService.enregistrer(formation);
            }
            experienceRepository.deleteByCvId(cv.getId());
            for (int i = 0; i < taille(experiencePoste); i++) {
                String poste = valeur(experiencePoste, i);
                if (poste == null) continue;
                ExperienceProfessionnelle experience = new ExperienceProfessionnelle();
                experience.setCv(cv);
                experience.setPoste(limiter(poste, 150));
                experience.setEntreprise(limiter(valeur(experienceEntreprise, i), 150));
                experience.setDateDebut(date(valeur(experienceDebut, i)));
                experience.setDateFin(date(valeur(experienceFin, i)));
                experienceService.enregistrer(experience);
            }
            competenceRepository.deleteByCvId(cv.getId());
            for (int i = 0; i < taille(competenceNom); i++) {
                String nom = valeur(competenceNom, i);
                if (nom == null) continue;
                Integer niveauId = competenceNiveau != null && i < competenceNiveau.size() ? competenceNiveau.get(i) : null;
                NiveauCompetence niveau = niveauId == null ? null : niveauRepository.findById(niveauId).orElse(null);
                if (niveau == null) throw new IllegalArgumentException("Sélectionnez un niveau valide pour chaque compétence.");
                Competence competence = new Competence();
                competence.setCv(cv);
                competence.setNomCompetence(limiter(nom, 100));
                competence.setNiveauCompetence(niveau);
                competenceService.enregistrer(competence);
            }
            return "redirect:/api/candidat/etape/" + ("precedent".equals(direction) ? "1" : "3");
        } catch (RuntimeException e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            ra.addFlashAttribute("erreur", message(e));
            return "redirect:/api/candidat/etape/2";
        }
    }

    @PostMapping(value = "/etape/3", consumes = "multipart/form-data")
    public String enregistrerDossier(@RequestParam(required = false) MultipartFile lettreMotivation,
            @RequestParam(required = false) List<MultipartFile> justificatifs,
            HttpSession session, RedirectAttributes ra) {
        try {
            Candidat candidat = candidatConnecte(session);
            if (lettreMotivation != null && !lettreMotivation.isEmpty()) {
                verifierFichier(lettreMotivation, Set.of("pdf", "docx"));
                session.setAttribute("lettreCandidature", enregistrerFichier(lettreMotivation, candidat.getId(), "lettre"));
            }
            List<String> noms = new ArrayList<>();
            Object existants = session.getAttribute("piecesCandidature");
            if (existants instanceof List<?> liste) for (Object item : liste) if (item instanceof String nom) noms.add(nom);
            long ajouts = justificatifs == null ? 0 : justificatifs.stream().filter(f -> f != null && !f.isEmpty()).count();
            if (noms.size() + ajouts > 10) throw new IllegalArgumentException("Le dossier peut contenir au maximum 10 justificatifs.");
            if (justificatifs != null) for (MultipartFile fichier : justificatifs) {
                if (fichier == null || fichier.isEmpty()) continue;
                verifierFichier(fichier, Set.of("pdf", "docx", "jpg", "jpeg", "png"));
                noms.add(enregistrerFichier(fichier, candidat.getId(), "piece"));
            }
            session.setAttribute("piecesCandidature", noms);
            ra.addFlashAttribute("succes", "Dossier enregistré. Vous pourrez le reprendre depuis cette étape.");
            return "redirect:/api/candidat/etape/3";
        } catch (RuntimeException e) {
            ra.addFlashAttribute("erreur", message(e));
            return "redirect:/api/candidat/etape/3";
        }
    }

    private Candidat candidatConnecte(HttpSession session) {
        SessionUser user = utilisateur(session);
        if (user == null || !"CANDIDAT".equals(user.getType()))
            throw new IllegalArgumentException("Créez votre compte avant de continuer.");
        return candidatService.trouverParId(user.getId());
    }
    private SessionUser utilisateur(HttpSession session) { return (SessionUser) session.getAttribute("user"); }
    private boolean estCandidat(HttpSession session) {
        SessionUser user = utilisateur(session);
        return user != null && "CANDIDAT".equals(user.getType());
    }
    private String enregistrerFichier(MultipartFile fichier, Integer candidatId, String prefixe) {
        try {
            Path dossier = uploadRoot.resolve(String.valueOf(candidatId)).normalize();
            if (!dossier.startsWith(uploadRoot)) throw new IllegalArgumentException("Chemin de fichier invalide.");
            Files.createDirectories(dossier);
            String extension = StringUtils.getFilenameExtension(fichier.getOriginalFilename());
            String nom = prefixe + "-" + UUID.randomUUID() + "." + extension.toLowerCase(Locale.ROOT);
            Files.copy(fichier.getInputStream(), dossier.resolve(nom));
            return nom;
        } catch (IOException e) {
            throw new IllegalArgumentException("Impossible d’enregistrer le fichier.", e);
        }
    }
    private void verifierFichier(MultipartFile fichier, Set<String> extensions) {
        String extension = StringUtils.getFilenameExtension(fichier.getOriginalFilename());
        if (extension == null || !extensions.contains(extension.toLowerCase(Locale.ROOT)))
            throw new IllegalArgumentException("Format de fichier non autorisé.");
        if (fichier.isEmpty() || fichier.getSize() > 10 * 1024 * 1024L)
            throw new IllegalArgumentException("Chaque fichier doit être non vide et faire au maximum 10 Mo.");
    }
    private static String limiter(String valeur, int max) {
        if (valeur == null || valeur.isBlank()) return null;
        String texte = valeur.trim();
        if (texte.length() > max) throw new IllegalArgumentException("Un champ dépasse " + max + " caractères.");
        return texte;
    }
    private static int taille(List<String> liste) { return liste == null ? 0 : liste.size(); }
    private static String valeur(List<String> liste, int index) {
        return liste == null || index >= liste.size() || liste.get(index) == null || liste.get(index).isBlank()
                ? null : liste.get(index).trim();
    }
    private static LocalDate date(String valeur) {
        if (valeur == null) return null;
        try { return LocalDate.parse(valeur); }
        catch (RuntimeException e) { throw new IllegalArgumentException("Une date saisie est invalide."); }
    }
    private static String message(RuntimeException e) {
        return e.getMessage() == null ? "Une erreur est survenue pendant l’enregistrement." : e.getMessage();
    }
}
