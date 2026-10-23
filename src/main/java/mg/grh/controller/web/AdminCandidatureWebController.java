package mg.grh.controller.web;

import jakarta.servlet.http.HttpSession;
import mg.grh.model.*;
import mg.grh.repository.*;
import mg.grh.service.CandidatureService;
import mg.grh.service.EntretienService;
import mg.grh.service.TestTechniqueService;
import mg.grh.service.PropositionService;
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
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Comparator;

/** Écrans RH du workflow de recrutement. L’accès /admin est contrôlé par AuthInterceptor. */
@Controller
@RequestMapping("/admin/candidatures")
public class AdminCandidatureWebController {
    private final CandidatureService candidatureService;
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
    private final PropositionService propositionService;
    private final Path uploadRoot;

    public AdminCandidatureWebController(CandidatureService candidatureService,
            DossierCandidatureRepository dossierRepository,
            DocumentDossierRepository documentRepository, CvRepository cvRepository,
            FormationRepository formationRepository, EducationRepository educationRepository,
            ExperienceProfessionnelleRepository experienceRepository, CompetenceRepository competenceRepository,
            CvLangueRepository cvLangueRepository, TestTechniqueService testService,
            EntretienService entretienService, PropositionEmbaucheRepository propositionRepository,
            StatutPropositionRepository statutPropositionRepository, ReponseCandidatRepository reponseRepository,
            DecisionRhRepository decisionRepository, NotificationRepository notificationRepository,
            PropositionService propositionService,
            @Value("${grh.upload-dir:uploads/candidatures}") String uploadDir) {
        this.candidatureService = candidatureService;
        this.dossierRepository = dossierRepository; this.documentRepository = documentRepository;
        this.cvRepository = cvRepository; this.formationRepository = formationRepository;
        this.educationRepository = educationRepository; this.experienceRepository = experienceRepository;
        this.competenceRepository = competenceRepository; this.cvLangueRepository = cvLangueRepository;
        this.testService = testService; this.entretienService = entretienService;
        this.propositionRepository = propositionRepository; this.statutPropositionRepository = statutPropositionRepository;
        this.reponseRepository = reponseRepository; this.decisionRepository = decisionRepository;
        this.notificationRepository = notificationRepository;
        this.propositionService = propositionService;
        this.uploadRoot = Paths.get(uploadDir).toAbsolutePath().normalize();
    }

    @GetMapping
    public String liste(Model model) {
        model.addAttribute("candidatures", candidatureService.toutes());
        return "admin/candidatures";
    }

    @GetMapping("/tests")
    public String tests(Model model) {
        Map<Integer, List<TestRow>> parOffre = new LinkedHashMap<>();
        for (Candidature candidature : candidatureService.toutes()) {
            if (!"TEST_TECHNIQUE".equals(candidature.getStatut().getLibelle())) continue;
            List<TestTechnique> tests = testService.pourCandidature(candidature.getId());
            if (tests.isEmpty()) continue;
            TestTechnique test = tests.get(tests.size() - 1);
            parOffre.computeIfAbsent(candidature.getOffre().getId(), ignored -> new java.util.ArrayList<>())
                    .add(new TestRow(candidature, test));
        }
        model.addAttribute("groupes", parOffre.values().stream()
                .map(rows -> new GroupeTests(rows.get(0).getCandidature().getOffre(), rows))
                .sorted(Comparator.comparing(g -> g.getOffre().getTitre(), String.CASE_INSENSITIVE_ORDER)).toList());
        return "admin/tests-candidats";
    }

    @GetMapping("/entretiens")
    public String entretiens(Model model) {
        Map<Integer, List<EntretienRow>> parOffre = new LinkedHashMap<>();
        for (Candidature candidature : candidatureService.toutes()) {
            if (!"ENTRETIEN".equals(candidature.getStatut().getLibelle())) continue;
            List<Entretien> entretiens = entretienService.pourCandidature(candidature.getId());
            if (entretiens.isEmpty()) continue;
            Entretien entretien = entretiens.get(entretiens.size() - 1);
            parOffre.computeIfAbsent(candidature.getOffre().getId(), ignored -> new java.util.ArrayList<>())
                    .add(new EntretienRow(candidature, entretien));
        }
        model.addAttribute("groupes", parOffre.values().stream()
                .map(rows -> new GroupeEntretiens(rows.get(0).getCandidature().getOffre(), rows))
                .sorted(Comparator.comparing(g -> g.getOffre().getTitre(), String.CASE_INSENSITIVE_ORDER)).toList());
        return "admin/entretiens-candidats";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        Candidature candidature = candidatureService.findById(id);
        model.addAttribute("candidature", candidature);
        model.addAttribute("dossier", dossierRepository.findByCandidatureId(id).orElse(null));
        model.addAttribute("proposition", propositionRepository.findByCandidatureId(id).orElse(null));
        List<TestTechnique> tests = testService.pourCandidature(id);
        model.addAttribute("tests", tests);
        model.addAttribute("testCourant", tests.isEmpty() ? null : tests.get(tests.size() - 1));
        boolean testAccepte = !tests.isEmpty() && tests.get(tests.size() - 1).isAccepteCandidat();
        model.addAttribute("testAccepte", testAccepte);
        model.addAttribute("testPlanifiable", java.util.Set.of("DOSSIER_RETENU", "TEST_TECHNIQUE")
                .contains(candidature.getStatut().getLibelle()));
        List<Entretien> entretiens = entretienService.pourCandidature(id);
        model.addAttribute("entretiens", entretiens);
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

    @PostMapping("/{id}/decision-dossier")
    @Transactional
    public String decisionDossier(@PathVariable Integer id, @RequestParam String decision, RedirectAttributes ra) {
        try {
            Candidature c = candidatureService.findById(id);
            if (!"RECUE".equals(c.getStatut().getLibelle()))
                throw new IllegalStateException("La décision sur le dossier a déjà été prise.");
            String nouveauStatut = switch (decision) {
                case "ACCEPTER" -> "DOSSIER_RETENU";
                case "REFUSER" -> "DOSSIER_REJETE";
                default -> throw new IllegalArgumentException("Décision invalide.");
            };
            c = candidatureService.changerStatut(id, nouveauStatut);
            notifier(c, "Décision après étude du dossier", "REFUSER".equals(decision)
                    ? "Votre dossier n’a pas été retenu pour l’offre « " + c.getOffre().getTitre() + " ». Vous ne pouvez plus poursuivre ce recrutement."
                    : "Votre dossier a été accepté pour l’offre « " + c.getOffre().getTitre() + " ». Le RH va programmer un test technique.");
            ra.addFlashAttribute("succes", "Dossier " + ("ACCEPTER".equals(decision) ? "accepté" : "refusé") + " et candidat notifié.");
        } catch (RuntimeException e) { TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); ra.addFlashAttribute("erreur", e.getMessage()); }
        return "redirect:/admin/candidatures/" + id;
    }

    @PostMapping("/{id}/decision-test")
    @Transactional
    public String decisionTest(@PathVariable Integer id, @RequestParam String decision, RedirectAttributes ra) {
        try {
            Candidature c = candidatureService.findById(id);
            if (!"TEST_TECHNIQUE".equals(c.getStatut().getLibelle()))
                throw new IllegalStateException("Cette candidature n’est pas à l’étape du test technique.");
            List<TestTechnique> tests = testService.pourCandidature(id);
            if (tests.isEmpty()) throw new IllegalStateException("Aucune convocation au test n’est enregistrée.");
            TestTechnique test = tests.get(tests.size() - 1);
            if (test.getResultatAccepte() != null) throw new IllegalStateException("Une décision a déjà été enregistrée pour ce test.");
            if ("ACCEPTER".equals(decision)) {
                test.setResultatAccepte(true);
                testService.update(test.getId(), test);
                notifier(c, "Test technique validé", "Votre résultat au test pour l’offre « " + c.getOffre().getTitre() + " » est accepté. Le RH peut maintenant programmer un entretien.");
                ra.addFlashAttribute("succes", "Test accepté. Le formulaire de programmation de l’entretien est disponible.");
            } else if ("REFUSER".equals(decision)) {
                test.setResultatAccepte(false);
                testService.update(test.getId(), test);
                c = candidatureService.changerStatut(id, "NON_RETENUE");
                notifier(c, "Décision après test technique", "Votre candidature n’a pas été retenue après le test pour l’offre « " + c.getOffre().getTitre() + " ». Vous ne pouvez plus poursuivre ce recrutement.");
                ra.addFlashAttribute("succes", "Candidat refusé après le test et notifié.");
            } else throw new IllegalArgumentException("Décision invalide.");
        } catch (RuntimeException e) { TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); ra.addFlashAttribute("erreur", e.getMessage()); }
        return "redirect:/admin/candidatures/tests";
    }

    @PostMapping("/{id}/decision-entretien")
    @Transactional
    public String decisionEntretien(@PathVariable Integer id, @RequestParam String decision, RedirectAttributes ra) {
        try {
            Candidature c = candidatureService.findById(id);
            if (!"ENTRETIEN".equals(c.getStatut().getLibelle()) || entretienService.pourCandidature(id).isEmpty())
                throw new IllegalStateException("Cette candidature ne dispose pas d’un entretien programmé.");
            if ("REFUSER".equals(decision)) {
                c = candidatureService.changerStatut(id, "NON_RETENUE");
                notifier(c, "Décision après entretien", "Votre candidature n’a pas été retenue après l’entretien pour l’offre « " + c.getOffre().getTitre() + " ». Vous ne pouvez plus poursuivre ce recrutement.");
                ra.addFlashAttribute("succes", "Candidat refusé après l’entretien et notifié.");
                return "redirect:/admin/candidatures/entretiens";
            }
            if (!"ACCEPTER".equals(decision)) throw new IllegalArgumentException("Décision invalide.");
            c = candidatureService.changerStatut(id, "RETENUE");
            PropositionEmbauche proposition = propositionService.creerPourCandidature(id);
            notifier(c, "Candidature retenue", "Votre candidature pour l’offre « " + c.getOffre().getTitre() + " » est retenue. Consultez la proposition d’embauche et indiquez votre réponse.", proposition.getId());
            ra.addFlashAttribute("succes", "Candidat retenu et notifié. Ouverture de la page d’acceptation existante.");
            return "redirect:/candidats/" + c.getCandidat().getId() + "/propositions/" + proposition.getId();
        } catch (RuntimeException e) { TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); ra.addFlashAttribute("erreur", e.getMessage()); }
        return "redirect:/admin/candidatures/entretiens";
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
            test.setAccepteCandidat(false);
            test.setResultatAccepte(null);
            if (modification) testService.update(test.getId(), test); else testService.create(test);
            c = candidatureService.changerStatut(id, "TEST_TECHNIQUE");
            notifier(c, modification ? "Test technique modifié" : "Test technique programmé",
                    (modification ? "La convocation au test technique a été modifiée. " : "Votre test technique est programmé. ")
                            + "Date : " + date + ", heure : " + heure + ", lieu : " + lieu
                            + ". Consignes : " + requis(consignes, "Les consignes du test sont obligatoires.")
                            + ". " + valeur(message));
            ra.addFlashAttribute("succes", modification
                    ? "Convocation au test mise à jour et candidat notifié. Il doit accepter la nouvelle convocation avant la planification de l’entretien."
                    : "Test technique planifié et candidat notifié.");
        } catch (RuntimeException e) { TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); ra.addFlashAttribute("erreur", e.getMessage()); }
        return "redirect:/admin/candidatures/" + id;
    }

    @PostMapping("/{id}/entretien")
    @Transactional
    public String planifierEntretien(@PathVariable Integer id, @RequestParam LocalDate date,
            @RequestParam LocalTime heure, @RequestParam String lieu, @RequestParam String consignes,
            @RequestParam(required = false) String message, @RequestParam(required = false) String origine, RedirectAttributes ra) {
        try {
            Candidature c = candidatureService.findById(id);
            List<TestTechnique> tests = testService.pourCandidature(id);
            if (!java.util.Set.of("TEST_TECHNIQUE", "ENTRETIEN").contains(c.getStatut().getLibelle())
                    || tests.isEmpty() || !Boolean.TRUE.equals(tests.get(tests.size() - 1).getResultatAccepte()))
                throw new IllegalStateException("Le RH doit accepter le résultat du test avant de planifier l’entretien.");
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
            notifier(c, modification ? "Entretien modifié" : "Entretien programmé",
                    (modification ? "La convocation à l’entretien a été modifiée. " : "Votre entretien est programmé. ")
                            + "Date : " + date + ", heure : " + heure + ", lieu : " + lieu
                            + ". Consignes : " + requis(consignes, "Les consignes sont obligatoires.")
                            + ". " + valeur(message));
            ra.addFlashAttribute("succes", modification
                    ? "Convocation à l’entretien mise à jour et candidat notifié."
                    : "Entretien planifié et candidat notifié.");
        } catch (RuntimeException e) { TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); ra.addFlashAttribute("erreur", e.getMessage()); }
        return "tests".equals(origine) ? "redirect:/admin/candidatures/tests" : "redirect:/admin/candidatures/" + id;
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
            p.setIdCandidature(c.getId()); p.setSalaireFinal(salaire); p.setDateEnvoi(LocalDateTime.now());
            p.setStatut(statutPropositionRepository.findByLibelle("EN_ATTENTE_CANDIDAT")
                    .orElseThrow(() -> new IllegalStateException("Statut de proposition non initialisé.")));
            propositionRepository.save(p);
            notifier(c, "Proposition d’embauche", "Une proposition d’embauche vous attend dans votre espace de suivi.", p.getId());
            ra.addFlashAttribute("succes", "Proposition transmise au candidat.");
            return "redirect:/candidats/" + c.getCandidat().getId() + "/propositions/" + p.getId();
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
            if (p.getDecisionRh() != null) throw new IllegalArgumentException("Une décision RH a déjà été enregistrée pour cette proposition.");
            String libelle = decision.trim().toUpperCase();
            if (!java.util.Set.of("ACCEPTEE", "REFUSEE").contains(libelle)) throw new IllegalArgumentException("Décision invalide.");
            p.setDecisionRh(decisionRepository.findByLibelle(libelle).orElseThrow(() -> new IllegalStateException("Décision RH non initialisée.")));
            p.setCommentaireRh(commentaire); p.setDateDecisionRh(LocalDateTime.now());
            p.setStatut(statutPropositionRepository.findByLibelle(libelle.equals("ACCEPTEE") ? "ACCORD_FINAL" : "REFUSEE_RH")
                    .orElseThrow(() -> new IllegalStateException("Statut final de proposition non initialisé.")));
            if (libelle.equals("ACCEPTEE") && p.getSalaireDemande() != null) p.setSalaireFinal(p.getSalaireDemande());
            propositionRepository.save(p);
            if (libelle.equals("REFUSEE")) candidatureService.changerStatut(id, "NON_RETENUE");
            notifier(c, "Décision concernant la proposition", "Le service RH a enregistré sa décision : " + libelle + ". " + valeur(commentaire));
            ra.addFlashAttribute("succes", "Décision finale enregistrée et notifiée au candidat.");
        } catch (RuntimeException e) { TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); ra.addFlashAttribute("erreur", e.getMessage()); }
        return "redirect:/admin/candidatures/" + id;
    }

    private void notifier(Candidature c, String titre, String message) {
        notifier(c, titre, message, null);
    }

    private void notifier(Candidature c, String titre, String message, Integer idProposition) {
        Notification n = Notification.pour(c.getCandidat().getId(), null, idProposition, titre, message);
        n.setIdCandidature(c.getId());
        n.setDateCreation(LocalDateTime.now()); n.setLu(false); notificationRepository.save(n);
    }
    private static String requis(String texte, String erreur) { if (texte == null || texte.isBlank()) throw new IllegalArgumentException(erreur); return texte.trim(); }
    private static String valeur(String texte) { return texte == null ? "" : texte.trim(); }

    public static final class TestRow {
        private final Candidature candidature;
        private final TestTechnique test;
        TestRow(Candidature candidature, TestTechnique test) { this.candidature = candidature; this.test = test; }
        public Candidature getCandidature() { return candidature; }
        public TestTechnique getTest() { return test; }
    }
    public static final class GroupeTests {
        private final OffreEmploi offre;
        private final List<TestRow> candidatures;
        GroupeTests(OffreEmploi offre, List<TestRow> candidatures) { this.offre = offre; this.candidatures = candidatures; }
        public OffreEmploi getOffre() { return offre; }
        public List<TestRow> getCandidatures() { return candidatures; }
    }
    public static final class EntretienRow {
        private final Candidature candidature;
        private final Entretien entretien;
        EntretienRow(Candidature candidature, Entretien entretien) { this.candidature = candidature; this.entretien = entretien; }
        public Candidature getCandidature() { return candidature; }
        public Entretien getEntretien() { return entretien; }
    }
    public static final class GroupeEntretiens {
        private final OffreEmploi offre;
        private final List<EntretienRow> candidatures;
        GroupeEntretiens(OffreEmploi offre, List<EntretienRow> candidatures) { this.offre = offre; this.candidatures = candidatures; }
        public OffreEmploi getOffre() { return offre; }
        public List<EntretienRow> getCandidatures() { return candidatures; }
    }
}
