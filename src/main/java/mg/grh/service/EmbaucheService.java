package mg.grh.service;

import mg.grh.dto.ApercuEmbauche;
import mg.grh.dto.ResultatEmbauche;
import mg.grh.model.*;
import mg.grh.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Embauche à la validation d'un contrat, selon le type du besoin auquel il répond :
 * <ul>
 *   <li>CREATION : le poste n'a personne ; le candidat est ajouté aux employés (siège libre, ou nouveau siège).</li>
 *   <li>REMPLACEMENT : le poste a un occupant ; il passe au statut SUSPENDU, quitte le poste et est inscrit dans
 *       l'historique des employés ; le candidat prend sa place.</li>
 * </ul>
 * Un « siège » est une ligne de poste_fille (poste dans un département) ; il est occupé s'il a un employé rattaché.
 */
@Service
@Transactional
public class EmbaucheService {

    public static final String CREATION = "CREATION";
    public static final String REMPLACEMENT = "REMPLACEMENT";

    private static final String ACTIF = "ACTIF";
    private static final String SUSPENDU = "SUSPENDU";
    private static final String POSTE_OCCUPE = "Occupé";
    private static final String POSTE_GELE = "Gelé";

    /** Décision prise à partir du besoin ; sert à la fois à l'aperçu et à l'exécution. */
    private record Plan(String type, BesoinInfo besoin, Employe dejaEmbauche, List<Employe> occupants, String blocage) {
    }

    private final ContratCandidatRepository contratRepo;
    private final EmployeRepository employeRepo;
    private final PosteFilleRepository posteFilleRepo;
    private final PosteRepository posteRepo;
    private final DepartementRepository deptRepo;
    private final StatutEmployeRepository statutEmployeRepo;
    private final StatutPosteRepository statutPosteRepo;
    private final HistoriqueEmployeRepository historiqueEmployeRepo;
    private final HistoriqueContratEmployeRepository historiqueContratRepo;

    public EmbaucheService(ContratCandidatRepository contratRepo, EmployeRepository employeRepo,
                           PosteFilleRepository posteFilleRepo, PosteRepository posteRepo,
                           DepartementRepository deptRepo, StatutEmployeRepository statutEmployeRepo,
                           StatutPosteRepository statutPosteRepo, HistoriqueEmployeRepository historiqueEmployeRepo,
                           HistoriqueContratEmployeRepository historiqueContratRepo) {
        this.contratRepo = contratRepo;
        this.employeRepo = employeRepo;
        this.posteFilleRepo = posteFilleRepo;
        this.posteRepo = posteRepo;
        this.deptRepo = deptRepo;
        this.statutEmployeRepo = statutEmployeRepo;
        this.statutPosteRepo = statutPosteRepo;
        this.historiqueEmployeRepo = historiqueEmployeRepo;
        this.historiqueContratRepo = historiqueContratRepo;
    }

    // ------------------------------------------------------------ aperçu (avant validation)
    @Transactional(readOnly = true)
    public ApercuEmbauche apercu(ContratCandidat cc) {
        Optional<Plan> opt;
        try {
            opt = planifier(cc);
        } catch (IllegalStateException e) {
            return new ApercuEmbauche(true, null, null, null, e.getMessage(), true);
        }
        if (opt.isEmpty()) {
            return ApercuEmbauche.nonLie();
        }
        Plan plan = opt.get();
        BesoinInfo b = plan.besoin();
        String nom = nomComplet(cc.getCandidat());
        String lieu = "au poste « " + b.getLibellePoste() + " » du département « " + b.getNomDept() + " »";

        if (plan.blocage() != null) {
            return new ApercuEmbauche(true, plan.type(), b.getLibellePoste(), b.getNomDept(), plan.blocage(), true);
        }
        String message;
        if (plan.dejaEmbauche() != null) {
            message = nom + " est déjà employé(e) pour ce besoin (contrat précédent) : ce contrat sera rattaché à son "
                    + "dossier, sans nouvelle embauche.";
        } else if (CREATION.equals(plan.type())) {
            message = "Création de poste : à la validation, " + nom + " sera ajouté(e) à la liste des employés " + lieu + ".";
        } else {
            message = "Remplacement : à la validation, " + plan.occupants().get(0).getNomEmploye()
                    + " (actuellement en poste) passera au statut SUSPENDU et sera transféré(e) dans l'historique des "
                    + "employés ; " + nom + " prendra sa place " + lieu + ".";
        }
        return new ApercuEmbauche(true, plan.type(), b.getLibellePoste(), b.getNomDept(), message, false);
    }

    /** Embauche enregistrée pour ce contrat (après validation). */
    @Transactional(readOnly = true)
    public Optional<HistoriqueContratEmploye> embauchePour(Integer contratCandidatId) {
        return historiqueContratRepo.findFirstByContratCandidatIdOrderByIdAsc(contratCandidatId);
    }

    // ------------------------------------------------------------ exécution (à la validation)
    /** Applique la règle du besoin. Lève IllegalStateException si les données sont incohérentes : rien n'est modifié. */
    public ResultatEmbauche embaucher(ContratCandidat cc) {
        Optional<Plan> opt = planifier(cc);
        if (opt.isEmpty()) {
            return ResultatEmbauche.aucun("Aucun besoin n'est lié à ce contrat : la liste des employés n'a pas été modifiée.");
        }
        Plan plan = opt.get();
        if (plan.blocage() != null) {
            throw new IllegalStateException(plan.blocage());
        }
        LocalDate debut = dateDebut(cc);
        BesoinInfo b = plan.besoin();

        if (plan.dejaEmbauche() != null) {
            lierContrat(plan.dejaEmbauche(), cc, debut);
            return new ResultatEmbauche(plan.type(), plan.dejaEmbauche().getNomEmploye()
                    + " était déjà employé(e) : ce contrat est rattaché à son dossier.", plan.dejaEmbauche(), null);
        }
        return CREATION.equals(plan.type()) ? creer(cc, b, debut) : remplacer(cc, plan, debut);
    }

    private ResultatEmbauche creer(ContratCandidat cc, BesoinInfo b, LocalDate debut) {
        Employe nouveau = nouvelEmploye(cc, siegeLibre(b), debut);
        return new ResultatEmbauche(CREATION, "Création de poste : " + nouveau.getNomEmploye()
                + " est ajouté(e) à la liste des employés au poste « " + b.getLibellePoste()
                + " » du département « " + b.getNomDept() + " ».", nouveau, null);
    }

    private ResultatEmbauche remplacer(ContratCandidat cc, Plan plan, LocalDate debut) {
        BesoinInfo b = plan.besoin();
        Employe ancien = plan.occupants().get(0);
        PosteFille siege = ancien.getPosteFille();

        // 1) l'occupant actuel est suspendu et libère le poste
        StatutEmploye suspendu = statutEmploye(SUSPENDU);
        ancien.setStatut(suspendu);
        ancien.setPosteFille(null);

        // 2) le candidat prend sa place
        Employe nouveau = nouvelEmploye(cc, siege, debut);

        // 3) l'ancien est transféré dans l'historique des employés
        HistoriqueEmploye h = new HistoriqueEmploye();
        h.setEmploye(ancien);
        h.setStatut(suspendu);
        h.setDateDebut(LocalDate.now());
        h.setMotif("Remplacé(e) par " + nouveau.getNomEmploye() + " au poste « " + b.getLibellePoste()
                + " » (département " + b.getNomDept() + ") – contrat " + cc.getTypeContrat() + " n°" + cc.getId());
        h.setDateEnregistrement(LocalDateTime.now());
        historiqueEmployeRepo.save(h);

        return new ResultatEmbauche(REMPLACEMENT, "Remplacement : " + ancien.getNomEmploye()
                + " est suspendu(e) et transféré(e) dans l'historique des employés ; " + nouveau.getNomEmploye()
                + " occupe désormais le poste « " + b.getLibellePoste() + " » du département « "
                + b.getNomDept() + " ».", nouveau, ancien);
    }

    // ------------------------------------------------------------ décision à partir du besoin
    private Optional<Plan> planifier(ContratCandidat cc) {
        Integer idCandidature = cc.getIdCandidature();
        if (idCandidature == null) {
            return Optional.empty();
        }
        BesoinInfo b = contratRepo.findBesoinInfo(idCandidature).orElseThrow(() -> new IllegalStateException(
                "Le besoin lié à ce contrat est introuvable (candidature n°" + idCandidature + ")."));
        String type = b.getTypeBesoin();

        Optional<HistoriqueContratEmploye> deja = historiqueContratRepo
                .findFirstByContratCandidatIdCandidatureOrderByIdAsc(idCandidature);
        if (deja.isPresent() && !deja.get().getContratCandidat().getId().equals(cc.getId())) {
            return Optional.of(new Plan(type, b, deja.get().getEmploye(), List.of(), null));
        }

        if (CREATION.equals(type)) {
            return Optional.of(new Plan(type, b, null, List.of(), null));
        }
        if (!REMPLACEMENT.equals(type)) {
            return Optional.of(new Plan(type, b, null, List.of(), "Type de besoin non géré : « " + type + " »."));
        }

        List<Employe> occupants = employeRepo.findByPosteFillePosteIdAndPosteFilleDeptId(b.getIdPoste(), b.getIdDept());
        String lieu = "le poste « " + b.getLibellePoste() + " » du département « " + b.getNomDept() + " »";
        String blocage = null;
        if (occupants.isEmpty()) {
            blocage = "Remplacement impossible : aucun employé n'occupe " + lieu + ". Vérifiez le besoin ou la liste des employés.";
        } else if (occupants.size() > 1) {
            blocage = "Remplacement ambigu : plusieurs employés occupent " + lieu + " ("
                    + occupants.stream().map(Employe::getNomEmploye).collect(Collectors.joining(", "))
                    + "). Le besoin ne précise pas lequel est remplacé.";
        }
        return Optional.of(new Plan(type, b, null, occupants, blocage));
    }

    // ------------------------------------------------------------ utilitaires privés
    /** Siège sans employé et non gelé ; à défaut, un nouveau siège (une création ajoute un poste). */
    private PosteFille siegeLibre(BesoinInfo b) {
        return posteFilleRepo.findByPosteIdAndDeptIdOrderById(b.getIdPoste(), b.getIdDept()).stream()
                .filter(s -> !estGele(s) && !employeRepo.existsByPosteFilleId(s.getId()))
                .findFirst()
                .orElseGet(() -> {
                    PosteFille s = new PosteFille();
                    s.setPoste(posteRepo.getReferenceById(b.getIdPoste()));
                    s.setDept(deptRepo.getReferenceById(b.getIdDept()));
                    return posteFilleRepo.save(s);
                });
    }

    private Employe nouvelEmploye(ContratCandidat cc, PosteFille siege, LocalDate debut) {
        Employe e = new Employe();
        e.setNomEmploye(nomComplet(cc.getCandidat()));
        e.setPosteFille(siege);
        e.setDateEmbauche(debut);
        e.setStatut(statutEmploye(ACTIF));
        e = employeRepo.save(e);
        lierContrat(e, cc, debut);
        statutPosteRepo.findByLibelle(POSTE_OCCUPE).ifPresent(siege::setStatutPoste);
        return e;
    }

    private void lierContrat(Employe employe, ContratCandidat cc, LocalDate date) {
        HistoriqueContratEmploye h = new HistoriqueContratEmploye();
        h.setEmploye(employe);
        h.setContratCandidat(cc);
        h.setDateEmbauche(date);
        historiqueContratRepo.save(h);
    }

    private boolean estGele(PosteFille s) {
        return s.getStatutPoste() != null && POSTE_GELE.equals(s.getStatutPoste().getLibelle());
    }

    private StatutEmploye statutEmploye(String libelle) {
        return statutEmployeRepo.findByLibelle(libelle).orElseThrow(() -> new IllegalStateException(
                "Statut d'employé '" + libelle + "' absent de statut_employe (jeu de données non chargé ?)"));
    }

    /** Date de début du contrat, sinon aujourd'hui. */
    private LocalDate dateDebut(ContratCandidat cc) {
        LocalDate d = switch (cc.getTypeContrat()) {
            case "CE" -> cc.getContratCe().getDateDebut();
            case "CDD" -> cc.getContratCdd().getDateDebut();
            default -> cc.getContratCdi().getDatePriseFonction();
        };
        return d != null ? d : LocalDate.now();
    }

    /** « Prénom Nom », limité aux 50 caractères de employe.nom_employe. */
    private String nomComplet(Candidat c) {
        String nom = ((c.getPrenomCandidat() == null ? "" : c.getPrenomCandidat() + " ")
                + (c.getNomCandidat() == null ? "" : c.getNomCandidat())).trim();
        return nom.length() > 50 ? nom.substring(0, 50) : nom;
    }
}
