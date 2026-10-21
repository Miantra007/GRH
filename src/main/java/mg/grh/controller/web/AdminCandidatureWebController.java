package mg.grh.controller.web;

import jakarta.servlet.http.HttpSession;
import mg.grh.model.*;
import mg.grh.repository.*;
import mg.grh.service.CandidatureService;
import mg.grh.service.EntretienService;
import mg.grh.service.TestTechniqueService;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ContentDisposition;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.net.MalformedURLException;

/** Écrans RH du workflow de recrutement. L’accès /admin est contrôlé par AuthInterceptor. */
@Controller
@RequestMapping("/admin/candidatures")
public class AdminCandidatureWebController {
    private final CandidatureService candidatureService;
    private final StatutCandidatureRepository statutRepository;
    private final DossierCandidatureRepository dossierRepository;
    private final DocumentDossierRepository documentRepository;
    private final CvRepository cvRepository;
    private final FormationRepository formationRepository;
    private final EducationRepository educationRepository;
    private final ExperienceProfessionnelleRepository experienceRepository;
    private final CompetenceRepository competenceRepository;
    private final CvLangueRepository cvLangueRepository;
    private final TestTechniqueService testService;
    private final EntretienService entretienService;
    private final PropositionEmbaucheRepository propositionRepository;
    private final StatutPropositionRepository statutPropositionRepository;
    private final ReponseCandidatRepository reponseRepository;
    private final DecisionRhRepository decisionRepository;
    private final NotificationRepository notificationRepository;
    private final Path uploadRoot;

    public AdminCandidatureWebController(CandidatureService candidatureService,
            StatutCandidatureRepository statutRepository, DossierCandidatureRepository dossierRepository,
            DocumentDossierRepository documentRepository, CvRepository cvRepository,
            FormationRepository formationRepository, EducationRepository educationRepository,
            ExperienceProfessionnelleRepository experienceRepository, CompetenceRepository competenceRepository,
            CvLangueRepository cvLangueRepository, TestTechniqueService testService,
            EntretienService entretienService, PropositionEmbaucheRepository propositionRepository,
            StatutPropositionRepository statutPropositionRepository, ReponseCandidatRepository reponseRepository,
            DecisionRhRepository decisionRepository, NotificationRepository notificationRepository,
            @Value("${grh.upload-dir:uploads/candidatures}") String uploadDir) {
        this.candidatureService = candidatureService; this.statutRepository = statutRepository;
        this.dossierRepository = dossierRepository; this.documentRepository = documentRepository;
        this.cvRepository = cvRepository; this.formationRepository = formationRepository;
        this.educationRepository = educationRepository; this.experienceRepository = experienceRepository;
        this.competenceRepository = competenceRepository; this.cvLangueRepository = cvLangueRepository;
        this.testService = testService; this.entretienService = entretienService;
        this.propositionRepository = propositionRepository; this.statutPropositionRepository = statutPropositionRepository;
        this.reponseRepository = reponseRepository; this.decisionRepository = decisionRepository;
        this.notificationRepository = notificationRepository;
        this.uploadRoot = Paths.get(uploadDir).toAbsolutePath().normalize();
    }

    @GetMapping
    public String liste(Model model) {
        model.addAttribute("candidatures", candidatureService.toutes());
        return "admin/candidatures";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        Candidature candidature = candidatureService.findById(id);
        model.addAttribute("candidature", candidature);
        model.addAttribute("statuts", statutRepository.findAll());
        model.addAttribute("dossier", dossierRepository.findByCandidatureId(id).orElse(null));
        model.addAttribute("proposition", propositionRepository.findByCandidatureId(id).orElse(null));
        model.addAttribute("tests", testService.pourCandidature(id));
        model.addAttribute("entretiens", entretienService.pourCandidature(id));
        cvRepository.findFirstByCandidatIdOrderByIdDesc(candidature.getCandidat().getId()).ifPresent(cv -> {
            model.addAttribute("cv", cv);
            model.addAttribute("formations", formationRepository.findByCvId(cv.getId()));
            model.addAttribute("educations", educationRepository.findByCvId(cv.getId()));
            model.addAttribute("experiences", experienceRepository.findByCvId(cv.getId()));
            model.addAttribute("competences", competenceRepository.findByCvId(cv.getId()));
            model.addAttribute("languesCv", cvLangueRepository.findByCvId(cv.getId()));
        });
        if (model.getAttribute("dossier") instanceof DossierCandidature dossier)
            model.addAttribute("documents", documentRepository.findByDossierId(dossier.getId()));
        return "admin/candidature-detail";
    }

    @GetMapping("/{id}/documents/{documentId}")
    public ResponseEntity<Resource> document(@PathVariable Integer id, @PathVariable Integer documentId) throws MalformedURLException {
        Candidature candidature = candidatureService.findById(id);
        DocumentDossier document = documentRepository.findById(documentId).orElse(null);
        if (document == null || document.getDossier() == null || document.getDossier().getCandidature() == null
                || !document.getDossier().getCandidature().getId().equals(candidature.getId())) return ResponseEntity.notFound().build();
        Path candidateDirectory = uploadRoot.resolve(String.valueOf(candidature.getCandidat().getId())).normalize();
        Path file = candidateDirectory.resolve(document.getCheminFichier()).normalize();
        if (!file.startsWith(candidateDirectory) || !Files.isRegularFile(file)) return ResponseEntity.notFound().build();
        Resource resource = new UrlResource(file.toUri());
        MediaType type;
        try { type = document.getTypeContenu() == null ? MediaType.APPLICATION_OCTET_STREAM : MediaType.parseMediaType(document.getTypeContenu()); }
        catch (RuntimeException e) { type = MediaType.APPLICATION_OCTET_STREAM; }
        return ResponseEntity.ok().contentType(type)
                .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment()
                        .filename(document.getNomFichier(), java.nio.charset.StandardCharsets.UTF_8).build().toString())
                .header("X-Content-Type-Options", "nosniff")
                .body(resource);
    }

    @PostMapping("/{id}/statut")
    @Transactional
    public String statut(@PathVariable Integer id, @RequestParam String statut, RedirectAttributes ra) {
        try {
            if (!java.util.Set.of("RECUE", "DOSSIER_RETENU", "DOSSIER_REJETE", "TEST_TECHNIQUE", "ENTRETIEN", "RETENUE", "NON_RETENUE").contains(statut))
                throw new IllegalArgumentException("Ce statut n’appartient pas au workflow de recrutement.");
            Candidature c = candidatureService.changerStatut(id, statut);
            notifier(c, "Évolution de votre candidature", "Le statut de votre candidature est maintenant : " + statut + ".");
            ra.addFlashAttribute("succes", "Statut de la candidature mis à jour.");
        } catch (RuntimeException e) { TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); ra.addFlashAttribute("erreur", e.getMessage()); }
        return "redirect:/admin/candidatures/" + id;
    }

    @PostMapping("/{id}/test")
    @Transactional
    public String planifierTest(@PathVariable Integer id, @RequestParam LocalDate date,
            @RequestParam LocalTime heure, @RequestParam String lieu, @RequestParam String consignes,
            @RequestParam(required = false) String message, RedirectAttributes ra) {
        try {
            Candidature c = candidatureService.findById(id);
            if (!java.util.Set.of("DOSSIER_RETENU", "TEST_TECHNIQUE").contains(c.getStatut().getLibelle()))
                throw new IllegalStateException("Ne planifiez un test qu’après avoir retenu le dossier.");
            if (date.isBefore(LocalDate.now()) || (date.equals(LocalDate.now()) && heure.isBefore(LocalTime.now())))
                throw new IllegalArgumentException("La date et l’heure du test doivent être dans le futur.");
            TestTechnique test = testService.pourCandidature(id).stream()
                    .max(java.util.Comparator.comparing(TestTechnique::getDateTest, java.util.Comparator.nullsFirst(java.util.Comparator.naturalOrder())))
                    .orElseGet(TestTechnique::new);
            boolean modification = test.getId() != null;
            test.setCandidature(c); test.setDateTest(date); test.setHeureTest(heure);
            test.setLieu(requis(lieu, "Le lieu du test est obligatoire.")); test.setConsignes(requis(consignes, "Les consignes du test sont obligatoires."));
            test.setDescription("Test technique"); test.setMessageCandidat(message);
            if (modification) testService.update(test.getId(), test); else testService.create(test);
            c = candidatureService.changerStatut(id, "TEST_TECHNIQUE");
            notifier(c, "Test technique planifié", "Le test est prévu le " + date + " à " + heure + " — " + lieu + ". " + valeur(message));
            ra.addFlashAttribute("succes", "Test technique planifié et notification ajoutée au suivi candidat.");
        } catch (RuntimeException e) { TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); ra.addFlashAttribute("erreur", e.getMessage()); }
        return "redirect:/admin/candidatures/" + id;
    }

    @PostMapping("/{id}/entretien")
    @Transactional
    public String planifierEntretien(@PathVariable Integer id, @RequestParam LocalDate date,
            @RequestParam LocalTime heure, @RequestParam String lieu, @RequestParam String consignes,
            @RequestParam(required = false) String message, RedirectAttributes ra) {
        try {
            Candidature c = candidatureService.findById(id);
            if (!java.util.Set.of("DOSSIER_RETENU", "TEST_TECHNIQUE", "ENTRETIEN").contains(c.getStatut().getLibelle()))
                throw new IllegalStateException("Ne planifiez un entretien qu’après la sélection du dossier.");
            if (date.isBefore(LocalDate.now()) || (date.equals(LocalDate.now()) && heure.isBefore(LocalTime.now())))
                throw new IllegalArgumentException("La date et l’heure de l’entretien doivent être dans le futur.");
            Entretien entretien = entretienService.pourCandidature(id).stream()
                    .max(java.util.Comparator.comparing(Entretien::getDateEntretien, java.util.Comparator.nullsFirst(java.util.Comparator.naturalOrder())))
                    .orElseGet(Entretien::new);
            boolean modification = entretien.getId() != null;
            entretien.setCandidature(c); entretien.setDateEntretien(date); entretien.setHeureEntretien(heure);
            entretien.setLieu(requis(lieu, "Le lieu de l’entretien est obligatoire.")); entretien.setConsignes(requis(consignes, "Les consignes sont obligatoires."));
            entretien.setDescription("Entretien"); entretien.setMessageCandidat(message);
            if (modification) entretienService.update(entretien.getId(), entretien); else entretienService.create(entretien);
            c = candidatureService.changerStatut(id, "ENTRETIEN");
            notifier(c, "Entretien planifié", "L’entretien est prévu le " + date + " à " + heure + " — " + lieu + ". " + valeur(message));
            ra.addFlashAttribute("succes", "Entretien planifié et notification ajoutée au suivi candidat.");
        } catch (RuntimeException e) { TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); ra.addFlashAttribute("erreur", e.getMessage()); }
        return "redirect:/admin/candidatures/" + id;
    }

    @PostMapping("/{id}/proposition")
    @Transactional
    public String envoyerProposition(@PathVariable Integer id, @RequestParam BigDecimal salaire,
            @RequestParam(required = false) String horaire, @RequestParam(required = false) String responsable,
            @RequestParam(required = false) String missions, @RequestParam(required = false) LocalDate datePrisePoste,
            RedirectAttributes ra) {
        try {
            Candidature c = candidatureService.findById(id);
            if (!"RETENUE".equals(c.getStatut().getLibelle())) throw new IllegalArgumentException("La candidature doit d’abord être marquée RETENUE.");
            if (salaire.signum() < 0) throw new IllegalArgumentException("Le salaire proposé doit être positif ou nul.");
            PropositionEmbauche p = propositionRepository.findByCandidatureId(id).orElseGet(PropositionEmbauche::new);
            if (p.getReponseCandidat() != null) throw new IllegalArgumentException("Le candidat a déjà répondu à cette proposition.");
            p.setCandidature(c); p.setSalairePropose(salaire); p.setHoraireTravail(horaire); p.setResponsableHierarchique(responsable);
            p.setTravailAEffectuer(missions); p.setDatePrisePoste(datePrisePoste); p.setDateEnvoi(LocalDateTime.now());
            p.setStatutProposition(statutPropositionRepository.findByLibelle("EN_ATTENTE_CANDIDAT")
                    .orElseThrow(() -> new IllegalStateException("Statut de proposition non initialisé.")));
            propositionRepository.save(p);
            notifier(c, "Proposition d’embauche", "Une proposition d’embauche vous attend dans votre espace de suivi.");
            ra.addFlashAttribute("succes", "Proposition transmise au candidat.");
        } catch (RuntimeException e) { TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); ra.addFlashAttribute("erreur", e.getMessage()); }
        return "redirect:/admin/candidatures/" + id;
    }

    @PostMapping("/{id}/proposition/decision")
    @Transactional
    public String decisionProposition(@PathVariable Integer id, @RequestParam String decision,
            @RequestParam(required = false) String commentaire, RedirectAttributes ra) {
        try {
            Candidature c = candidatureService.findById(id);
            PropositionEmbauche p = propositionRepository.findByCandidatureId(id).orElseThrow(() -> new IllegalArgumentException("Proposition introuvable."));
            if (p.getReponseCandidat() == null) throw new IllegalArgumentException("Attendez la réponse du candidat avant de décider.");
            String libelle = decision.trim().toUpperCase();
            if (!java.util.Set.of("ACCEPTEE", "REFUSEE").contains(libelle)) throw new IllegalArgumentException("Décision invalide.");
            p.setDecisionRh(decisionRepository.findByLibelle(libelle).orElseThrow(() -> new IllegalStateException("Décision RH non initialisée.")));
            p.setCommentaireRh(commentaire); p.setDateDecisionRh(LocalDateTime.now());
            p.setStatutProposition(statutPropositionRepository.findByLibelle(libelle.equals("ACCEPTEE") ? "ACCORD_FINAL" : "REFUSEE_RH")
                    .orElseThrow(() -> new IllegalStateException("Statut final de proposition non initialisé.")));
            if (libelle.equals("ACCEPTEE")) p.setSalaireFinal(p.getSalaireDemande() == null ? p.getSalairePropose() : p.getSalaireDemande());
            propositionRepository.save(p);
            if (libelle.equals("REFUSEE")) candidatureService.changerStatut(id, "NON_RETENUE");
            notifier(c, "Décision concernant la proposition", "Le service RH a enregistré sa décision : " + libelle + ". " + valeur(commentaire));
            ra.addFlashAttribute("succes", "Décision finale enregistrée et notifiée au candidat.");
        } catch (RuntimeException e) { TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); ra.addFlashAttribute("erreur", e.getMessage()); }
        return "redirect:/admin/candidatures/" + id;
    }

    private void notifier(Candidature c, String titre, String message) {
        Notification n = new Notification(); n.setCandidat(c.getCandidat()); n.setCandidature(c); n.setTitre(titre); n.setMessage(message);
        n.setDateCreation(LocalDateTime.now()); n.setLu(false); notificationRepository.save(n);
    }
    private static String requis(String texte, String erreur) { if (texte == null || texte.isBlank()) throw new IllegalArgumentException(erreur); return texte.trim(); }
    private static String valeur(String texte) { return texte == null ? "" : texte.trim(); }
}
