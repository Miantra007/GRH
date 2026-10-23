package mg.grh.service;

import mg.grh.dto.BesoinCreationForm;
import mg.grh.dto.BesoinDetailsForm;
import mg.grh.dto.DecisionFinanceForm;
import mg.grh.exception.ResourceNotFoundException;
import mg.grh.model.*;
import mg.grh.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class BesoinService extends GenericService<Besoin> {
    private final BesoinRepository besoinRepository;
    private final DirectionRepository directionRepository;
    private final DepartementRepository departementRepository;
    private final ResponsableRepository responsableRepository;
    private final TypeBesoinRepository typeBesoinRepository;
    private final StatutBesoinRepository statutBesoinRepository;
    private final PosteRepository posteRepository;
    private final BesoinPosteRepository besoinPosteRepository;
    private final OffreEmploiRepository offreEmploiRepository;
    private final StatutOffreRepository statutOffreRepository;
    private final NotificationRepository notificationRepository;

    public BesoinService(BesoinRepository repository, DirectionRepository directionRepository,
            DepartementRepository departementRepository, ResponsableRepository responsableRepository,
            TypeBesoinRepository typeBesoinRepository, StatutBesoinRepository statutBesoinRepository,
            PosteRepository posteRepository,
            BesoinPosteRepository besoinPosteRepository, OffreEmploiRepository offreEmploiRepository,
            StatutOffreRepository statutOffreRepository, NotificationRepository notificationRepository) {
        super(repository);
        this.besoinRepository = repository;
        this.directionRepository = directionRepository;
        this.departementRepository = departementRepository;
        this.responsableRepository = responsableRepository;
        this.typeBesoinRepository = typeBesoinRepository;
        this.statutBesoinRepository = statutBesoinRepository;
        this.posteRepository = posteRepository;
        this.besoinPosteRepository = besoinPosteRepository;
        this.offreEmploiRepository = offreEmploiRepository;
        this.statutOffreRepository = statutOffreRepository;
        this.notificationRepository = notificationRepository;
    }

    /** Crée un besoin et ses postes. Toute erreur annule l'ensemble des insertions. */
    @Transactional
    public Besoin creerPourDirecteur(Integer directeurId, BesoinCreationForm formulaire) {
        if (formulaire == null || formulaire.getPostes() == null || formulaire.getPostes().isEmpty()) {
            throw new IllegalArgumentException("Ajoutez au moins un poste au besoin.");
        }
        Direction direction = directionRepository.findById(formulaire.getDirectionId())
                .orElseThrow(() -> new ResourceNotFoundException("Direction introuvable."));
        if (direction.getDirecteur() == null || !directeurId.equals(direction.getDirecteur().getId())) {
            throw new SecurityException("Vous ne pouvez créer un besoin que pour votre propre direction.");
        }

        Besoin besoin = new Besoin();
        besoin.setDirection(direction);
        besoin.setDepartement(departementRepository.findById(formulaire.getDepartementId())
                .orElseThrow(() -> new ResourceNotFoundException("Département introuvable.")));
        besoin.setDirecteurDemandeur(responsableRepository.findById(directeurId)
                .orElseThrow(() -> new ResourceNotFoundException("Directeur introuvable.")));
        besoin.setTypeBesoin(typeBesoinRepository.findById(formulaire.getTypeBesoinId())
                .orElseThrow(() -> new ResourceNotFoundException("Type de besoin introuvable.")));
        besoin.setStatutBesoin(statutBesoinRepository.findByLibelle("EN_ATTENTE_RH")
                .orElseThrow(() -> new IllegalStateException("Le statut EN_ATTENTE_RH doit être initialisé.")));
        besoin.setMotif(formulaire.getMotif() == null ? null : formulaire.getMotif().trim());
        besoin.setDateDemande(LocalDateTime.now());
        besoin.setVisibleClient(false);
        besoin.validerPourCreation();
        besoin = repository.save(besoin);

        java.util.Set<Integer> postesChoisis = new java.util.HashSet<>();
        for (BesoinCreationForm.LignePoste ligne : formulaire.getPostes()) {
            if (ligne == null) {
                throw new IllegalArgumentException("Une ligne de poste est invalide.");
            }
            ligne.valider();
            Poste poste = posteRepository.findById(ligne.getPosteId())
                    .orElseThrow(() -> new ResourceNotFoundException("Poste introuvable."));
            if (poste.getDirection() == null || !direction.getId().equals(poste.getDirection().getId())) {
                throw new SecurityException("Le poste « " + poste.getLibelle() + " » n'appartient pas à votre direction.");
            }
            if (!postesChoisis.add(poste.getId())) {
                throw new IllegalArgumentException("Le poste « " + poste.getLibelle() + " » est demandé deux fois : indiquez plutôt le nombre de postes.");
            }

            BesoinPoste besoinPoste = new BesoinPoste();
            besoinPoste.setBesoin(besoin);
            besoinPoste.setPoste(poste);
            besoinPoste.setNombrePostes(ligne.getNombrePostes());
            besoinPoste.setMissions(ligne.getMissions());
            besoinPoste.setHoraireTravail(ligne.getHoraireTravail());
            besoinPoste.setResponsableHierarchique(ligne.getResponsableHierarchique());
            besoinPoste.setDatePrisePoste(ligne.getDatePrisePoste());
            besoinPosteRepository.save(besoinPoste);
        }
        return besoin;
    }

    @Transactional(readOnly = true)
    public java.util.List<Besoin> enAttenteRh() {
        return besoinRepository.findByStatutBesoinLibelleOrderByDateDemandeAsc("EN_ATTENTE_RH");
    }

    @Transactional(readOnly = true)
    public java.util.List<Besoin> enAttenteFinance() {
        return besoinRepository.findByStatutBesoinLibelleOrderByDateDemandeAsc("EN_ATTENTE_FINANCE");
    }

    @Transactional(readOnly = true)
    public java.util.List<Besoin> enAttenteDg() {
        return besoinPosteRepository.findBesoinsAvecPosteEnAttenteDg();
    }

    @Transactional(readOnly = true)
    public java.util.List<BesoinPoste> postesDuBesoin(Integer besoinId) {
        findById(besoinId);
        return besoinPosteRepository.findByBesoinIdOrderByIdAsc(besoinId);
    }

    /**
     * À la première analyse, tous les postes sont présentés à RH. Lors d'un retour
     * Finance, seuls les postes refusés peuvent être modifiés.
     */
    @Transactional(readOnly = true)
    public java.util.List<BesoinPoste> postesARetraiterParRh(Integer besoinId) {
        java.util.List<BesoinPoste> postes = postesDuBesoin(besoinId);
        boolean retourFinance = postes.stream().anyMatch(poste -> poste.getDecisionFinance() != null);
        return retourFinance
                ? postes.stream().filter(poste -> "REFUSEE".equals(poste.getDecisionFinance())).toList()
                : postes;
    }

    /** Postes dont la Finance n'a pas encore fixé le salaire retenu. */
    @Transactional(readOnly = true)
    public java.util.List<BesoinPoste> postesADeciderParFinance(Integer besoinId) {
        return postesDuBesoin(besoinId).stream()
                .filter(poste -> poste.getSalaireValideFinance() == null)
                .toList();
    }

    /** Postes dont le salaire est fixé par la Finance (accepté ou contre-proposé) et pas encore validés par le DG. */
    @Transactional(readOnly = true)
    public java.util.List<BesoinPoste> postesAValiderParDg(Integer besoinId) {
        return postesDuBesoin(besoinId).stream()
                .filter(poste -> poste.getSalaireValideFinance() != null)
                .filter(poste -> poste.getDecisionDg() == null)
                .toList();
    }

    @Transactional(readOnly = true)
    public BesoinDetailsForm formulaireRh(Integer besoinId) {
        BesoinDetailsForm formulaire = new BesoinDetailsForm();
        for (BesoinPoste besoinPoste : postesARetraiterParRh(besoinId)) {
            BesoinDetailsForm.Ligne ligne = new BesoinDetailsForm.Ligne();
            ligne.setBesoinPosteId(besoinPoste.getId());
            ligne.setSalaireProposeRh(besoinPoste.getSalaireProposeRh());
            offreEmploiRepository.findByBesoinPosteId(besoinPoste.getId()).ifPresent(offre -> {
                ligne.setTitre(offre.getTitre()); ligne.setDescription(offre.getDescription());
                ligne.setProfilRecherche(offre.getProfilRecherche()); ligne.setDateLimite(offre.getDateLimite());
            });
            formulaire.getLignes().add(ligne);
        }
        return formulaire;
    }

    @Transactional
    public void traiterParRh(Integer besoinId, Integer rhId, BesoinDetailsForm formulaire) {
        Besoin besoin = besoinAvecStatut(besoinId, "EN_ATTENTE_RH");
        if (formulaire == null || formulaire.getLignes() == null || formulaire.getLignes().isEmpty()) {
            throw new IllegalArgumentException("Complétez le formulaire de chaque poste.");
        }
        java.util.Map<Integer, BesoinPoste> postes = postesARetraiterParRh(besoinId).stream()
                .collect(java.util.stream.Collectors.toMap(BesoinPoste::getId, p -> p));
        verifierLignesAttendues(formulaire.getLignes().stream()
                .map(BesoinDetailsForm.Ligne::getBesoinPosteId).toList(), postes);
        for (BesoinDetailsForm.Ligne ligne : formulaire.getLignes()) {
            BesoinPoste besoinPoste = postes.get(ligne.getBesoinPosteId());
            if (besoinPoste == null || ligne.getSalaireProposeRh() == null || ligne.getSalaireProposeRh().signum() < 0
                    || ligne.getTitre() == null || ligne.getTitre().isBlank()) {
                throw new IllegalArgumentException("Chaque poste doit avoir un titre et un salaire proposé valide.");
            }
            besoinPoste.setSalaireProposeRh(ligne.getSalaireProposeRh());
            besoinPoste.setSalaireValideFinance(null);
            besoinPoste.setDecisionFinance(null);
            besoinPoste.setCommentaireFinance(null);
            besoinPoste.setDecisionDg(null);
            besoinPosteRepository.save(besoinPoste);
            OffreEmploi offre = offreEmploiRepository.findByBesoinPosteId(besoinPoste.getId()).orElseGet(OffreEmploi::new);
            offre.setBesoinPosteId(besoinPoste.getId());
            offre.setTitre(ligne.getTitre().trim()); offre.setDescription(ligne.getDescription());
            offre.setProfilRecherche(ligne.getProfilRecherche()); offre.setDateLimite(ligne.getDateLimite());
            offre.setStatutOffre(statutOffreRepository.findByLibelle("BROUILLON")
                    .orElseThrow(() -> new IllegalStateException("Le statut BROUILLON doit être initialisé.")));
            offre.setVisibleClient(false);
            offreEmploiRepository.save(offre);
        }
        besoin.setRhTraitant(responsable(rhId, "RH"));
        besoin.setCommentaireRh(formulaire.getCommentaireRh());
        besoin.setDateTraitementRh(LocalDateTime.now());
        besoin.setStatutBesoin(statut("EN_ATTENTE_FINANCE"));
        repository.save(besoin);
    }

    /**
     * Décision de la Finance, poste par poste :
     * ACCEPTEE -> le salaire proposé par le RH est retenu ;
     * REFUSEE  -> la Finance propose son propre salaire, et c'est celui-là qui est retenu.
     * Une fois tous les salaires fixés, le besoin part directement à la Direction générale.
     */
    @Transactional
    public void deciderParFinance(Integer besoinId, Integer financeId, DecisionFinanceForm formulaire) {
        Besoin besoin = besoinAvecStatut(besoinId, "EN_ATTENTE_FINANCE");
        java.util.List<BesoinPoste> tousLesPostes = postesDuBesoin(besoinId);
        java.util.Map<Integer, BesoinPoste> postes = tousLesPostes.stream()
                .filter(poste -> poste.getSalaireValideFinance() == null)
                .collect(java.util.stream.Collectors.toMap(BesoinPoste::getId, p -> p));
        if (formulaire == null || formulaire.getLignes() == null || formulaire.getLignes().size() != postes.size()) {
            throw new IllegalArgumentException("Décidez le salaire de chaque poste.");
        }
        verifierLignesAttendues(formulaire.getLignes().stream()
                .map(DecisionFinanceForm.Ligne::getBesoinPosteId).toList(), postes);
        for (DecisionFinanceForm.Ligne ligne : formulaire.getLignes()) {
            BesoinPoste besoinPoste = postes.get(ligne.getBesoinPosteId());
            String decision = ligne.getDecision() == null ? "" : ligne.getDecision().trim().toUpperCase();
            if (besoinPoste == null || !(decision.equals("ACCEPTEE") || decision.equals("REFUSEE"))) {
                throw new IllegalArgumentException("Chaque salaire doit être accepté ou refusé.");
            }
            java.math.BigDecimal salaireRetenu;
            if (decision.equals("ACCEPTEE")) {
                salaireRetenu = besoinPoste.getSalaireProposeRh();
            } else {
                salaireRetenu = ligne.getSalaireFinance();
                if (salaireRetenu == null || salaireRetenu.signum() < 0) {
                    throw new IllegalArgumentException("Proposez un salaire valide pour le poste « "
                            + besoinPoste.getPoste().getLibelle() + " » dont vous refusez le salaire RH.");
                }
            }
            besoinPoste.setDecisionFinance(decision);
            besoinPoste.setCommentaireFinance(ligne.getCommentaire());
            besoinPoste.setSalaireValideFinance(salaireRetenu);
            besoinPoste.setDecisionDg(null);
            besoinPosteRepository.save(besoinPoste);
        }
        besoin.setFinanceValidateur(responsable(financeId, "FINANCE"));
        besoin.setCommentaireFinance(formulaire.getCommentaireFinance());
        besoin.setDateValidationFinance(LocalDateTime.now());
        boolean toutFixe = tousLesPostes.stream().allMatch(poste -> poste.getSalaireValideFinance() != null);
        besoin.setStatutBesoin(statut(toutFixe ? "EN_ATTENTE_DG" : "EN_ATTENTE_FINANCE"));
        repository.save(besoin);
    }

    @Transactional
    public void validerParDg(Integer besoinId, Integer dgId) {
        Besoin besoin = findById(besoinId);
        java.util.List<BesoinPoste> postesAValider = postesAValiderParDg(besoinId);
        if (postesAValider.isEmpty()) {
            throw new IllegalStateException("Aucun poste dont la Finance a fixé le salaire n'est en attente du DG.");
        }
        postesAValider.forEach(poste -> {
            poste.setDecisionDg("VALIDEE");
            besoinPosteRepository.save(poste);
        });
        besoin.setDgValidateur(responsable(dgId, "DG"));
        boolean toutValide = postesDuBesoin(besoinId).stream()
                .allMatch(poste -> poste.getSalaireValideFinance() != null
                        && "VALIDEE".equals(poste.getDecisionDg()));
        if (toutValide) {
            besoin.setDateValidationDg(LocalDateTime.now());
            besoin.setStatutBesoin(statut("VALIDEE"));
            notifierRhOffresAPublier(besoin, postesAValider);
        }
        repository.save(besoin);
    }

    /** Prévient le RH qui a traité le besoin (ou, à défaut, tous les RH) que les offres peuvent être publiées. */
    private void notifierRhOffresAPublier(Besoin besoin, java.util.List<BesoinPoste> postes) {
        java.util.List<mg.grh.model.Responsable> destinataires = besoin.getRhTraitant() != null
                ? java.util.List.of(besoin.getRhTraitant())
                : responsableRepository.findByRoleLibelle("Administrateur RH");
        String libelles = postes.stream().map(poste -> poste.getPoste().getLibelle())
                .collect(java.util.stream.Collectors.joining(", "));
        String message = "La Direction générale a validé le besoin n°" + besoin.getId() + " (" + libelles + "). "
                + "Les offres d'emploi correspondantes sont prêtes : publiez-les depuis le menu « Offres à publier ».";
        destinataires.forEach(rh -> notificationRepository.save(Notification.pour(null, rh.getId(), null,
                "Besoin validé : offres à publier", message)));
    }

    private Besoin besoinAvecStatut(Integer id, String statutAttendu) {
        Besoin besoin = findById(id);
        if (!statutAttendu.equals(besoin.getStatutBesoin().getLibelle())) {
            throw new IllegalStateException("Ce besoin n'est pas dans l'état attendu : " + statutAttendu + ".");
        }
        return besoin;
    }

    private StatutBesoin statut(String libelle) {
        return statutBesoinRepository.findByLibelle(libelle)
                .orElseThrow(() -> new IllegalStateException("Le statut " + libelle + " doit être initialisé."));
    }

    private void verifierLignesAttendues(java.util.List<Integer> idsSaisis,
            java.util.Map<Integer, BesoinPoste> postesAttendus) {
        java.util.Set<Integer> idsUniques = new java.util.HashSet<>(idsSaisis);
        if (idsUniques.size() != idsSaisis.size() || !idsUniques.equals(postesAttendus.keySet())) {
            throw new IllegalArgumentException("Les postes soumis ne correspondent pas aux postes à traiter.");
        }
    }

    private mg.grh.model.Responsable responsable(Integer id, String roleAttendu) {
        mg.grh.model.Responsable responsable = responsableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Responsable introuvable."));
        String libelleRole = responsable.getRole() == null ? null : responsable.getRole().getLibelle();
        if (!roleAttendu.equals(typeUtilisateur(libelleRole))) {
            throw new SecurityException("Le rôle utilisateur ne permet pas cette action.");
        }
        return responsable;
    }

    /** Convertit les libellés métier enregistrés en base en types utilisés par la session. */
    private String typeUtilisateur(String libelleRole) {
        if (libelleRole == null) return "";
        return switch (libelleRole) {
            case "Administrateur RH" -> "RH";
            case "Direction générale" -> "DG";
            case "Directeur" -> "DIRECTEUR";
            case "Financier" -> "FINANCE";
            default -> "";
        };
    }
}
