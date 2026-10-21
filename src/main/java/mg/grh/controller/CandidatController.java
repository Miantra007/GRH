package mg.grh.controller;

import jakarta.servlet.http.HttpSession;
import mg.grh.model.*;
import mg.grh.repository.*;
import mg.grh.service.CandidatService;
import mg.grh.service.CvService;
import mg.grh.service.FormationService;
import mg.grh.service.ExperienceProfessionnelleService;
import mg.grh.service.CompetenceService;
import mg.grh.service.EducationService;
import mg.grh.service.CvLangueService;
import mg.grh.service.OffreEmploiService;
import mg.grh.service.CandidatureService;
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
import java.io.Serializable;

/** Parcours candidat en trois étapes, arrêté avant la soumission d'une candidature. */
@Controller
@RequestMapping("/api/candidat")
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
    private final OffreEmploiService offreService;
    private final CandidatureService candidatureService;
    private final EducationRepository educationRepository;
    private final CvLangueRepository cvLangueRepository;
    private final LangueRepository langueRepository;
    private final NiveauLangueRepository niveauLangueRepository;
    private final DossierCandidatureRepository dossierRepository;
    private final DocumentDossierRepository documentRepository;
    private final TestTechniqueRepository testTechniqueRepository;
    private final EntretienRepository entretienRepository;
    private final PropositionEmbaucheRepository propositionRepository;
    private final NotificationRepository notificationRepository;
    private final ReponseCandidatRepository reponseRepository;
    private final StatutPropositionRepository statutPropositionRepository;
    private final EducationService educationService;
    private final CvLangueService cvLangueService;
    private final Path uploadRoot;

    public CandidatController(CandidatService candidatService, CvRepository cvRepository, CvService cvService,
            FormationRepository formationRepository, ExperienceProfessionnelleRepository experienceRepository,
            CompetenceRepository competenceRepository, NiveauCompetenceRepository niveauRepository,
            FormationService formationService, ExperienceProfessionnelleService experienceService,
            CompetenceService competenceService, OffreEmploiService offreService,
            CandidatureService candidatureService, EducationRepository educationRepository,
            CvLangueRepository cvLangueRepository, LangueRepository langueRepository,
            NiveauLangueRepository niveauLangueRepository, DossierCandidatureRepository dossierRepository,
            DocumentDossierRepository documentRepository, TestTechniqueRepository testTechniqueRepository,
            EntretienRepository entretienRepository, PropositionEmbaucheRepository propositionRepository,
            NotificationRepository notificationRepository, ReponseCandidatRepository reponseRepository,
            StatutPropositionRepository statutPropositionRepository, EducationService educationService,
            CvLangueService cvLangueService,
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
        this.offreService = offreService;
        this.candidatureService = candidatureService;
        this.educationRepository = educationRepository;
        this.cvLangueRepository = cvLangueRepository;
        this.langueRepository = langueRepository;
        this.niveauLangueRepository = niveauLangueRepository;
        this.dossierRepository = dossierRepository;
        this.documentRepository = documentRepository;
        this.testTechniqueRepository = testTechniqueRepository;
        this.entretienRepository = entretienRepository;
        this.propositionRepository = propositionRepository;
        this.notificationRepository = notificationRepository;
        this.reponseRepository = reponseRepository;
        this.statutPropositionRepository = statutPropositionRepository;
        this.educationService = educationService;
        this.cvLangueService = cvLangueService;
        this.uploadRoot = Paths.get(uploadDir).toAbsolutePath().normalize();
    }

    @GetMapping
    public String accueil() {
        return "redirect:/api/candidat/offres";
    }

    @GetMapping("/offres")
    public String offres(@RequestParam(required = false) String recherche, Model model) {
        List<OffreEmploi> toutes = offreService.offresPubliees();
        if (recherche != null && !recherche.isBlank()) {
            String q = recherche.trim().toLowerCase(Locale.ROOT);
            toutes = toutes.stream().filter(o -> (o.getTitre() != null && o.getTitre().toLowerCase(Locale.ROOT).contains(q))
                    || (o.getDescription() != null && o.getDescription().toLowerCase(Locale.ROOT).contains(q))
                    || (o.getProfilRecherche() != null && o.getProfilRecherche().toLowerCase(Locale.ROOT).contains(q))).toList();
        }
        model.addAttribute("offres", toutes);
        model.addAttribute("recherche", recherche);
        return "candidat/offres";
    }

    @GetMapping("/offres/{offreId}")
    public String detailOffre(@PathVariable Integer offreId, Model model) {
        model.addAttribute("offre", offreService.offrePubliee(offreId));
        return "candidat/offre-detail";
    }

    @GetMapping("/offres/{offreId}/postuler")
    public String postuler(@PathVariable Integer offreId, HttpSession session) {
        OffreEmploi offre = offreService.offrePubliee(offreId);
        session.setAttribute("candidatureOffreId", offre.getId());
        session.removeAttribute("candidatureCandidatId");
        session.removeAttribute("lettreCandidatureDraft");
        session.removeAttribute("documentsCandidatureDraft");
        return "redirect:/api/candidat/etape/1";
    }

    @GetMapping("/etape/{etape}")
    public String afficherEtape(@PathVariable int etape, HttpSession session, Model model) {
        if (etape < 1 || etape > 3) return "redirect:/api/candidat/etape/1";
        if (etape > 1 && candidatIdAnonyme(session) == null)
            return "redirect:/api/candidat/etape/1";
        model.addAttribute("etape", etape);
        if (candidatIdAnonyme(session) != null) {
            Candidat candidat = candidatService.trouverParId(candidatIdAnonyme(session));
            model.addAttribute("candidat", candidat);
            Cv cv = cvRepository.findFirstByCandidatIdOrderByIdDesc(candidat.getId()).orElse(null);
            model.addAttribute("cv", cv);
            if (cv != null) {
                model.addAttribute("formations", formationRepository.findByCvId(cv.getId()));
                model.addAttribute("experiences", experienceRepository.findByCvId(cv.getId()));
                model.addAttribute("competences", competenceRepository.findByCvId(cv.getId()));
                model.addAttribute("educations", educationRepository.findByCvId(cv.getId()));
                model.addAttribute("languesCv", cvLangueRepository.findByCvId(cv.getId()));
            }
            model.addAttribute("cvImporte", session.getAttribute("cvImporteCandidature"));
            model.addAttribute("lettre", session.getAttribute("lettreCandidature"));
            model.addAttribute("pieces", session.getAttribute("piecesCandidature"));
            model.addAttribute("lettreDraft", session.getAttribute("lettreCandidatureDraft"));
            model.addAttribute("documentsDraft", session.getAttribute("documentsCandidatureDraft"));
        }
        Integer offreId = (Integer) session.getAttribute("candidatureOffreId");
        if (offreId == null) return "redirect:/api/candidat/offres";
        model.addAttribute("offre", offreService.offrePubliee(offreId));
        List<NiveauCompetence> niveaux = niveauRepository.findAll();
        if (niveaux.isEmpty()) {
            for (String libelle : List.of("Débutant", "Intermédiaire", "Avancé", "Expert")) {
                NiveauCompetence niveau = new NiveauCompetence();
                niveau.setLibelleNiveau(libelle);
                niveaux.add(niveauRepository.save(niveau));
            }
        }
        model.addAttribute("niveaux", niveaux);
        List<NiveauLangue> niveauxLangue = niveauLangueRepository.findAll();
        if (niveauxLangue.isEmpty()) {
            for (String libelle : List.of("Débutant", "Intermédiaire", "Avancé", "Courant", "Natif")) {
                NiveauLangue niveau = new NiveauLangue(); niveau.setLibelleNiveau(libelle);
                niveauxLangue.add(niveauLangueRepository.save(niveau));
            }
        }
        model.addAttribute("niveauxLangue", niveauxLangue);
        model.addAttribute("langues", langueRepository.findAll());
        return "candidat/saisie-candidat";
    }

    @PostMapping("/etape/1")
    public String enregistrerCandidat(@RequestParam String nom, @RequestParam String prenom,
            @RequestParam String email,
            @RequestParam(required = false) LocalDate dtn, HttpSession session, RedirectAttributes ra) {
        try {
            Integer offreId = (Integer) session.getAttribute("candidatureOffreId");
            if (offreId == null) throw new IllegalArgumentException("Choisissez une offre avant de postuler.");
            offreService.offrePubliee(offreId);
            Candidat candidat = candidatService.trouverParEmail(email.trim()).orElseGet(Candidat::new);
            if (candidatureService.dejaPostule(offreId, candidat.getId()))
                throw new IllegalArgumentException("Une candidature existe déjà pour cette offre et cette adresse email.");
            candidat.setNomCandidat(nom);
            candidat.setPrenomCandidat(prenom);
            candidat.setEmail(email);
            candidat.setDtn(dtn);
            String motDePasseTechnique = candidat.getId() == null ? UUID.randomUUID().toString() : null;
            candidat = candidatService.enregistrerCandidat(candidat, motDePasseTechnique);
            session.setAttribute("candidatureCandidatId", candidat.getId());
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
            @RequestParam(required = false) List<String> diplome,
            @RequestParam(required = false) List<String> diplomeEtablissement,
            @RequestParam(required = false) List<String> diplomeDomaine,
            @RequestParam(required = false) List<String> diplomeNiveau,
            @RequestParam(required = false) List<String> diplomeDebut,
            @RequestParam(required = false) List<String> diplomeFin,
            @RequestParam(required = false) List<String> experiencePoste,
            @RequestParam(required = false) List<String> experienceEntreprise,
            @RequestParam(required = false) List<String> experienceDebut,
            @RequestParam(required = false) List<String> experienceFin,
            @RequestParam(required = false) List<String> competenceNom,
            @RequestParam(required = false) List<Integer> competenceNiveau,
            @RequestParam(required = false) List<String> langueNom,
            @RequestParam(required = false) List<Integer> langueNiveau,
            @RequestParam(required = false) MultipartFile cvImporte,
            @RequestParam(required = false, defaultValue = "suivant") String direction,
            HttpSession session, RedirectAttributes ra) {
        try {
            Candidat candidat = candidatConnecte(session);
            verifierPeriodes(formationDebut, formationFin);
            verifierPeriodes(experienceDebut, experienceFin);
            verifierPeriodes(diplomeDebut, diplomeFin);
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
            educationRepository.deleteByCvId(cv.getId());
            for (int i = 0; i < taille(diplome); i++) {
                String libelle = valeur(diplome, i);
                if (libelle == null) continue;
                Education education = new Education();
                education.setCv(cv);
                education.setDiplome(limiter(libelle, 150));
                education.setEtablissement(limiter(valeur(diplomeEtablissement, i), 150));
                education.setDomaineEtude(limiter(valeur(diplomeDomaine, i), 150));
                education.setNiveau(limiter(valeur(diplomeNiveau, i), 50));
                education.setDateDebut(date(valeur(diplomeDebut, i)));
                education.setDateFin(date(valeur(diplomeFin, i)));
                educationService.create(education);
            }
            cvLangueRepository.deleteByCvId(cv.getId());
            Set<Integer> languesAjoutees = new HashSet<>();
            for (int i = 0; i < taille(langueNom); i++) {
                String nomLangue = valeur(langueNom, i);
                if (nomLangue == null) continue;
                Langue langue = langueRepository.findByNomLangueIgnoreCase(nomLangue).orElseGet(() -> {
                    Langue nouvelle = new Langue(); nouvelle.setNomLangue(limiter(nomLangue, 100));
                    return langueRepository.save(nouvelle);
                });
                Integer niveauId = langueNiveau != null && i < langueNiveau.size() ? langueNiveau.get(i) : null;
                NiveauLangue niveau = niveauId == null ? null : niveauLangueRepository.findById(niveauId).orElse(null);
                if (niveau == null) throw new IllegalArgumentException("Sélectionnez un niveau valide pour chaque langue.");
                if (!languesAjoutees.add(langue.getId())) throw new IllegalArgumentException("Une même langue ne peut être ajoutée deux fois.");
                CvLangue lien = new CvLangue(); lien.setCv(cv); lien.setLangue(langue); lien.setNiveauLangue(niveau);
                cvLangueService.create(lien);
            }
            return "redirect:/api/candidat/etape/" + ("precedent".equals(direction) ? "1" : "3");
        } catch (RuntimeException e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            ra.addFlashAttribute("erreur", message(e));
            return "redirect:/api/candidat/etape/2";
        }
    }

    @PostMapping(value = "/etape/3", consumes = "multipart/form-data")
    @Transactional
    public String envoyerCandidature(@RequestParam(required = false) String lettreMotivation,
            @RequestParam(required = false) List<MultipartFile> justificatifs,
            @RequestParam(required = false, defaultValue = "envoyer") String direction,
            HttpSession session, RedirectAttributes ra) {
        try {
            Candidat candidat = candidatConnecte(session);
            Integer offreId = (Integer) session.getAttribute("candidatureOffreId");
            if (offreId == null) throw new IllegalArgumentException("L’offre sélectionnée a expiré.");
            String lettre = lettreMotivation == null || lettreMotivation.isBlank()
                    ? Objects.toString(session.getAttribute("lettreCandidatureDraft"), "") : lettreMotivation.trim();
            if (!direction.equals("precedent")) {
                if (lettre.isBlank()) throw new IllegalArgumentException("La lettre de motivation est obligatoire.");
                if (lettre.length() > 20000) throw new IllegalArgumentException("La lettre de motivation ne peut dépasser 20 000 caractères.");
            }
            List<Map<String, String>> documents = lireDocumentsDraft(session);
            long nouveaux = justificatifs == null ? 0 : justificatifs.stream().filter(f -> f != null && !f.isEmpty()).count();
            if (documents.size() + nouveaux > 10) throw new IllegalArgumentException("Le dossier peut contenir au maximum 10 justificatifs.");
            if (justificatifs != null) for (MultipartFile fichier : justificatifs) {
                if (fichier == null || fichier.isEmpty()) continue;
                verifierFichier(fichier, Set.of("pdf", "docx", "jpg", "jpeg", "png"));
                String nomEnregistre = enregistrerFichier(fichier, candidat.getId(), "piece");
                String nomOriginal = StringUtils.getFilename(StringUtils.cleanPath(Objects.requireNonNullElse(fichier.getOriginalFilename(), "document")));
                documents.add(Map.of("nom", Objects.requireNonNullElse(nomOriginal, "document"), "fichier", nomEnregistre,
                        "type", typeContenu(nomEnregistre)));
            }
            if (direction.equals("precedent")) {
                session.setAttribute("lettreCandidatureDraft", Objects.requireNonNullElse(lettreMotivation, ""));
                session.setAttribute("documentsCandidatureDraft", (Serializable) new ArrayList<>(documents));
                return "redirect:/api/candidat/etape/2";
            }
            Cv cv = cvRepository.findFirstByCandidatIdOrderByIdDesc(candidat.getId()).orElse(null);
            if (cv == null)
                throw new IllegalArgumentException("Complétez l’étape CV avant d’envoyer votre candidature.");
            boolean cvContenu = (cv.getProfilProfessionnel() != null && !cv.getProfilProfessionnel().isBlank())
                    || session.getAttribute("cvImporteCandidature") != null
                    || !formationRepository.findByCvId(cv.getId()).isEmpty()
                    || !educationRepository.findByCvId(cv.getId()).isEmpty()
                    || !experienceRepository.findByCvId(cv.getId()).isEmpty()
                    || !competenceRepository.findByCvId(cv.getId()).isEmpty()
                    || !cvLangueRepository.findByCvId(cv.getId()).isEmpty();
            if (!cvContenu) throw new IllegalArgumentException("Ajoutez un profil, une formation, un diplôme, une expérience, une compétence, une langue ou importez votre CV.");
            OffreEmploi offre = offreService.offrePubliee(offreId);
            Candidature candidature = candidatureService.deposer(offre, candidat);
            DossierCandidature dossier = new DossierCandidature();
            dossier.setCandidature(candidature);
            dossier.setLettreMotivation(lettre);
            dossier.setCvImporte(Objects.toString(session.getAttribute("cvImporteCandidature"), null));
            dossier.setDateCreation(LocalDateTime.now());
            dossier = dossierRepository.save(dossier);
            String cvImporteNom = dossier.getCvImporte();
            if (cvImporteNom != null && !cvImporteNom.isBlank()) {
                DocumentDossier cvPiece = new DocumentDossier();
                cvPiece.setDossier(dossier); cvPiece.setNomFichier("CV importé - " + cvImporteNom);
                cvPiece.setCheminFichier(cvImporteNom);
                cvPiece.setTypeContenu(cvImporteNom.toLowerCase(Locale.ROOT).endsWith(".pdf") ? "application/pdf" : "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
                cvPiece.setTailleOctets(tailleFichier(candidat.getId(), cvImporteNom));
                documentRepository.save(cvPiece);
            }
            for (Map<String, String> doc : documents) {
                DocumentDossier piece = new DocumentDossier();
                piece.setDossier(dossier); piece.setNomFichier(doc.get("nom"));
                piece.setCheminFichier(doc.get("fichier")); piece.setTypeContenu(doc.get("type"));
                piece.setTailleOctets(tailleFichier(candidat.getId(), doc.get("fichier")));
                documentRepository.save(piece);
            }
            session.setAttribute("codeSuiviDerniereCandidature", candidature.getCodeSuivi());
            session.removeAttribute("candidatureCandidatId");
            session.removeAttribute("candidatureOffreId");
            session.removeAttribute("lettreCandidatureDraft");
            session.removeAttribute("documentsCandidatureDraft");
            ra.addFlashAttribute("codeSuivi", candidature.getCodeSuivi());
            return "redirect:/api/candidat/confirmation/" + candidature.getCodeSuivi();
        } catch (RuntimeException e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            ra.addFlashAttribute("erreur", message(e));
            ra.addFlashAttribute("lettreDraft", lettreMotivation);
            return "redirect:/api/candidat/etape/3";
        }
    }

    @GetMapping("/confirmation/{code}")
    public String confirmation(@PathVariable String code, Model model) {
        Candidature candidature = candidatureService.parCodeSuivi(code);
        model.addAttribute("candidature", candidature);
        return "candidat/confirmation";
    }

    @GetMapping("/suivi/{code}")
    public String suivi(@PathVariable String code, Model model) {
        Candidature candidature = candidatureService.parCodeSuivi(code);
        model.addAttribute("candidature", candidature);
        model.addAttribute("tests", testTechniqueRepository.findByCandidatureIdOrderByDateTestAsc(candidature.getId()));
        model.addAttribute("entretiens", entretienRepository.findByCandidatureIdOrderByDateEntretienAsc(candidature.getId()));
        model.addAttribute("notifications", notificationRepository.findByCandidatureIdOrderByDateCreationDesc(candidature.getId()));
        model.addAttribute("proposition", propositionRepository.findByCandidatureId(candidature.getId()).orElse(null));
        model.addAttribute("dossier", dossierRepository.findByCandidatureId(candidature.getId()).orElse(null));
        return "candidat/suivi-candidature";
    }

    @GetMapping("/suivi")
    public String rechercherSuivi(@RequestParam String code) {
        String token = code.trim();
        candidatureService.parCodeSuivi(token);
        return "redirect:/api/candidat/suivi/" + token;
    }

    @PostMapping("/suivi/{code}/proposition")
    @Transactional
    public String repondreProposition(@PathVariable String code, @RequestParam String reponse,
            @RequestParam(required = false) String contreProposition, @RequestParam(required = false) String commentaire,
            @RequestParam(required = false) java.math.BigDecimal salaireDemande, RedirectAttributes ra) {
        try {
            Candidature candidature = candidatureService.parCodeSuivi(code);
            PropositionEmbauche proposition = propositionRepository.findByCandidatureId(candidature.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Aucune proposition n’est disponible."));
            if (proposition.getReponseCandidat() != null) throw new IllegalArgumentException("Une réponse a déjà été transmise.");
            String libelle = reponse.trim().toUpperCase(Locale.ROOT);
            if (!Set.of("OUI", "NON", "AUTRE").contains(libelle)) throw new IllegalArgumentException("Réponse invalide.");
            if (salaireDemande != null && salaireDemande.signum() < 0) throw new IllegalArgumentException("Le salaire demandé doit être positif ou nul.");
            ReponseCandidat ref = reponseRepository.findByLibelle(libelle).orElseThrow(() -> new IllegalStateException("Référentiel de réponse non initialisé."));
            if (libelle.equals("AUTRE") && (contreProposition == null || contreProposition.isBlank()) && salaireDemande == null)
                throw new IllegalArgumentException("Précisez votre contre-proposition ou le salaire demandé.");
            proposition.setReponseCandidat(ref); proposition.setContreProposition(limiter(contreProposition, 10000));
            proposition.setCommentaireCandidat(limiter(commentaire, 10000)); proposition.setSalaireDemande(salaireDemande);
            proposition.setDateReponseCandidat(LocalDateTime.now());
            if (libelle.equals("NON")) proposition.setStatutProposition(statutPropositionRepository.findByLibelle("REFUSEE_CANDIDAT").orElse(proposition.getStatutProposition()));
            else proposition.setStatutProposition(statutPropositionRepository.findByLibelle("EN_ATTENTE_RH").orElse(proposition.getStatutProposition()));
            propositionRepository.save(proposition);
            ra.addFlashAttribute("succes", "Votre réponse a été transmise au service RH.");
        } catch (RuntimeException e) { TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); ra.addFlashAttribute("erreur", message(e)); }
        return "redirect:/api/candidat/suivi/" + code;
    }

    private Candidat candidatConnecte(HttpSession session) {
        Integer candidatId = candidatIdAnonyme(session);
        if (candidatId == null) throw new IllegalArgumentException("Commencez par choisir une offre et saisir vos informations.");
        return candidatService.trouverParId(candidatId);
    }
    private Integer candidatIdAnonyme(HttpSession session) {
        Object id = session.getAttribute("candidatureCandidatId");
        return id instanceof Integer candidatId ? candidatId : null;
    }
    @SuppressWarnings("unchecked")
    private List<Map<String, String>> lireDocumentsDraft(HttpSession session) {
        Object contenu = session.getAttribute("documentsCandidatureDraft");
        return contenu instanceof List<?> liste ? (List<Map<String, String>>) liste : new ArrayList<>();
    }
    private long tailleFichier(Integer candidatId, String nom) {
        try { return Files.size(uploadRoot.resolve(String.valueOf(candidatId)).resolve(nom).normalize()); }
        catch (IOException e) { throw new IllegalArgumentException("Un justificatif temporaire est introuvable.", e); }
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
        try {
            byte[] signature = fichier.getBytes();
            boolean valide = switch (extension.toLowerCase(Locale.ROOT)) {
                case "pdf" -> signature.length >= 5 && signature[0] == '%' && signature[1] == 'P' && signature[2] == 'D' && signature[3] == 'F' && signature[4] == '-';
                case "docx" -> signature.length >= 4 && signature[0] == 'P' && signature[1] == 'K';
                case "jpg", "jpeg" -> signature.length >= 3 && (signature[0] & 0xff) == 0xff && (signature[1] & 0xff) == 0xd8 && (signature[2] & 0xff) == 0xff;
                case "png" -> signature.length >= 8 && (signature[0] & 0xff) == 0x89 && signature[1] == 'P' && signature[2] == 'N' && signature[3] == 'G';
                default -> false;
            };
            if (!valide) throw new IllegalArgumentException("Le contenu du fichier ne correspond pas à son extension.");
        } catch (IOException e) { throw new IllegalArgumentException("Impossible de vérifier le fichier transmis.", e); }
    }
    private static String limiter(String valeur, int max) {
        if (valeur == null || valeur.isBlank()) return null;
        String texte = valeur.trim();
        if (texte.length() > max) throw new IllegalArgumentException("Un champ dépasse " + max + " caractères.");
        return texte;
    }
    private static int taille(List<String> liste) { return liste == null ? 0 : liste.size(); }
    private static String typeContenu(String nom) {
        String lower = nom.toLowerCase(Locale.ROOT);
        if (lower.endsWith(".pdf")) return "application/pdf";
        if (lower.endsWith(".docx")) return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
        if (lower.endsWith(".png")) return "image/png";
        if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) return "image/jpeg";
        return "application/octet-stream";
    }
    private static String valeur(List<String> liste, int index) {
        return liste == null || index >= liste.size() || liste.get(index) == null || liste.get(index).isBlank()
                ? null : liste.get(index).trim();
    }
    private static LocalDate date(String valeur) {
        if (valeur == null) return null;
        try { return LocalDate.parse(valeur); }
        catch (RuntimeException e) { throw new IllegalArgumentException("Une date saisie est invalide."); }
    }
    private static void verifierPeriodes(List<String> debuts, List<String> fins) {
        for (int i = 0; i < taille(debuts); i++) {
            LocalDate debut = date(valeur(debuts, i));
            LocalDate fin = date(valeur(fins, i));
            if (debut != null && fin != null && fin.isBefore(debut))
                throw new IllegalArgumentException("La date de fin doit être égale ou postérieure à la date de début.");
        }
    }
    private static String message(RuntimeException e) {
        return e.getMessage() == null ? "Une erreur est survenue pendant l’enregistrement." : e.getMessage();
    }
}
