package mg.grh.service;

import mg.grh.dto.ResultatEmbauche;
import mg.grh.dto.ResultatValidation;
import mg.grh.exception.ResourceNotFoundException;
import mg.grh.model.*;
import mg.grh.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

/**
 * Cycle de vie d'un contrat :
 *   (Candidat)         choix/création -> statut "Créé" (après accord final)
 *   (Candidat)         signature -> statut "Signé"
 *   (RH)               validation-> statut "Validé"
 */
@Service
@Transactional
public class ContratCandidatService {

    public static final String CREE = "Créé";
    public static final String SIGNE = "Signé";
    public static final String VALIDE = "Validé";

    private final ContratCandidatRepository contratCandidatRepo;
    private final ContratCeRepository ceRepo;
    private final ContratCddRepository cddRepo;
    private final ContratCdiRepository cdiRepo;
    private final CandidatRepository candidatRepo;
    private final StatutContratRepository statutRepo;
    private final HistoriqueStatutContratRepository historiqueRepo;
    private final EmbaucheService embaucheService;
    private final PropositionEmbaucheRepository propositionRepo;
    private final NotificationRepository notificationRepo;
    private final TravailleurRepository travailleurRepo;
    private final PosteRepository posteRepo;

    public ContratCandidatService(ContratCandidatRepository contratCandidatRepo, ContratCeRepository ceRepo,
                                  ContratCddRepository cddRepo, ContratCdiRepository cdiRepo,
                                  CandidatRepository candidatRepo, StatutContratRepository statutRepo,
                                  HistoriqueStatutContratRepository historiqueRepo, EmbaucheService embaucheService,
                                  PropositionEmbaucheRepository propositionRepo, NotificationRepository notificationRepo,
                                  TravailleurRepository travailleurRepo, PosteRepository posteRepo) {
        this.contratCandidatRepo = contratCandidatRepo;
        this.ceRepo = ceRepo;
        this.cddRepo = cddRepo;
        this.cdiRepo = cdiRepo;
        this.candidatRepo = candidatRepo;
        this.statutRepo = statutRepo;
        this.historiqueRepo = historiqueRepo;
        this.embaucheService = embaucheService;
        this.propositionRepo = propositionRepo;
        this.notificationRepo = notificationRepo;
        this.travailleurRepo = travailleurRepo;
        this.posteRepo = posteRepo;
    }

    // ------------------------------------------------------------ lecture
    @Transactional(readOnly = true)
    public List<ContratCandidat> findAll() {
        return contratCandidatRepo.findAll();
    }

    @Transactional(readOnly = true)
    public ContratCandidat findById(Integer id) {
        return contratCandidatRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contrat introuvable : id=" + id));
    }

    @Transactional(readOnly = true)
    public List<ContratCandidat> findByCandidat(Integer candidatId) {
        return contratCandidatRepo.findByCandidatId(candidatId);
    }

    /** Contrats signés par le candidat, en attente de validation par le RH. */
    @Transactional(readOnly = true)
    public List<ContratCandidat> findAValider() {
        return contratCandidatRepo.findAll().stream()
                .filter(cc -> SIGNE.equals(getStatut(cc).getLibelle()))
                .toList();
    }

    /** Candidatures du candidat dont la proposition d'embauche est acceptée : un contrat peut y répondre. */
    @Transactional(readOnly = true)
    public List<CandidatureRetenueInfo> candidaturesRetenues(Integer candidatId) {
        return contratCandidatRepo.findCandidaturesRetenues(candidatId);
    }

    @Transactional(readOnly = true)
    public List<HistoriqueStatutContrat> historique(Integer contratCandidatId) {
        return historiqueRepo.findByContratCandidatIdOrderByDateChangementAsc(contratCandidatId);
    }

    // ------------------------------------------------------------ 1) création (statut Créé)
    public ContratCandidat creerCe(Integer candidatId, ContratCe ce) {
        return creerCe(candidatId, ce, null);
    }

    public ContratCandidat creerCdd(Integer candidatId, ContratCdd cdd) {
        return creerCdd(candidatId, cdd, null);
    }

    public ContratCandidat creerCdi(Integer candidatId, ContratCdi cdi) {
        return creerCdi(candidatId, cdi, null);
    }

    /** candidatureId : candidature (donc besoin) à laquelle répond le contrat ; null = aucun besoin lié. */
    public ContratCandidat creerCe(Integer candidatId, ContratCe ce, Integer candidatureId) {
        PropositionEmbauche proposition = verifierCandidature(candidatId, candidatureId);
        ce.setId(null);
        ce.setStatut(statut(CREE));
        return lier(candidatId, "CE", ceRepo.save(ce), null, null, candidatureId, proposition);
    }

    public ContratCandidat creerCe(Integer candidatId, Integer propositionId, ContratCe ce) {
        PropositionEmbauche p = propositionAcceptee(candidatId, propositionId);
        PropositionInfo info = propositionRepo.findInfo(propositionId).orElseThrow();
        ce.setRemunerationNette(p.getSalaireFinal()); ce.setPoste(info.getTitreOffre());
        ce.setMissions(info.getMissions()); ce.setDateDebut(date(info.getDatePrisePoste()));
        ce.setHeuresHebdo(heures(info.getHoraireTravail())); verifierTravailleur(candidatId, ce.getTravailleur());
        ce.setId(null); ce.setStatut(statut(CREE));
        return lier(candidatId, "CE", ceRepo.save(ce), null, null, p.getIdCandidature(), p);
    }

    public ContratCandidat creerCdd(Integer candidatId, ContratCdd cdd, Integer candidatureId) {
        PropositionEmbauche proposition = verifierCandidature(candidatId, candidatureId);
        cdd.setId(null);
        cdd.setStatut(statut(CREE));
        return lier(candidatId, "CDD", null, cddRepo.save(cdd), null, candidatureId, proposition);
    }

    public ContratCandidat creerCdd(Integer candidatId, Integer propositionId, ContratCdd cdd) {
        PropositionEmbauche p = propositionAcceptee(candidatId, propositionId);
        PropositionInfo info = propositionRepo.findInfo(propositionId).orElseThrow();
        cdd.setSalaireBase(p.getSalaireFinal());
        cdd.setPoste(posteRepo.findByLibelleIgnoreCase(info.getTitreOffre())
                .orElseThrow(() -> new IllegalStateException("Le poste de la proposition est introuvable dans le référentiel des postes.")));
        cdd.setDateDebut(date(info.getDatePrisePoste())); cdd.setHoraireTravail(info.getHoraireTravail());
        verifierTravailleur(candidatId, cdd.getTravailleur()); cdd.setId(null); cdd.setStatut(statut(CREE));
        return lier(candidatId, "CDD", null, cddRepo.save(cdd), null, p.getIdCandidature(), p);
    }

    public ContratCandidat creerCdi(Integer candidatId, ContratCdi cdi, Integer candidatureId) {
        PropositionEmbauche proposition = verifierCandidature(candidatId, candidatureId);
        cdi.setId(null);
        cdi.setStatut(statut(CREE));
        return lier(candidatId, "CDI", null, null, cdiRepo.save(cdi), candidatureId, proposition);
    }

    public ContratCandidat creerCdi(Integer candidatId, Integer propositionId, ContratCdi cdi) {
        PropositionEmbauche p = propositionAcceptee(candidatId, propositionId);
        PropositionInfo info = propositionRepo.findInfo(propositionId).orElseThrow();
        cdi.setSalaireBrutMensuel(p.getSalaireFinal()); cdi.setDatePriseFonction(date(info.getDatePrisePoste()));
        cdi.setEmploi(info.getTitreOffre()); cdi.setDureeHebdoHeures(heures(info.getHoraireTravail()));
        verifierTravailleur(candidatId, cdi.getTravailleur()); cdi.setId(null); cdi.setStatut(statut(CREE));
        return lier(candidatId, "CDI", null, null, cdiRepo.save(cdi), p.getIdCandidature(), p);
    }

    // ------------------------------------------------------------ 2) signature par le candidat (statut Signé)
    public ContratCandidat signer(Integer contratCandidatId, Integer candidatId) {
        ContratCandidat cc = findById(contratCandidatId);
        if (!cc.getCandidat().getId().equals(candidatId)) {
            throw new SecurityException("Ce contrat n'appartient pas à ce candidat");
        }
        exigerStatut(cc, CREE, "signer");
        changerStatut(cc, SIGNE);
        marquerDateSignature(cc);
        if (cc.getProposition() != null) {
            PropositionInfo info = propositionRepo.findInfo(cc.getProposition().getId()).orElse(null);
            if (info != null && info.getIdRhTraitant() != null)
                notificationRepo.save(Notification.pour(null, info.getIdRhTraitant(), cc.getProposition().getId(),
                        "Contrat signé par le candidat", "Le candidat a signé son contrat. Vous pouvez le valider."));
        }
        return cc;
    }

    // ------------------------------------------------------------ 3) validation par la Direction générale (statut Validé)
    public ContratCandidat valider(Integer contratCandidatId) {
        return validerEtEmbaucher(contratCandidatId).contrat();
    }

    /** Validation de la tâche Contrat : le RH responsable valide, sans traiter l'embauche. */
    public ContratCandidat valider(Integer contratCandidatId, Integer rhId) {
        ContratCandidat cc = findById(contratCandidatId);
        if (cc.getProposition() == null || cc.getIdCandidature() == null)
            throw new IllegalStateException("Le contrat doit être lié à une proposition acceptée.");
        PropositionInfo info = propositionRepo.findInfo(cc.getProposition().getId()).orElseThrow();
        if (!rhId.equals(info.getIdRhTraitant())) throw new SecurityException("Seul le RH chargé de cette proposition peut valider le contrat.");
        exigerStatut(cc, SIGNE, "valider"); changerStatut(cc, VALIDE);
        notificationRepo.save(Notification.pour(cc.getCandidat().getId(), null, cc.getProposition().getId(),
                "Contrat validé par le RH", "Votre contrat est validé. Consultez « Mon intégration » pour les consignes à suivre."));
        return cc;
    }

    /**
     * Valide le contrat puis applique la règle du besoin (création : le candidat devient employé ;
     * remplacement : l'occupant est suspendu et archivé, le candidat prend sa place).
     * Tout est annulé si l'embauche est impossible : le contrat reste "Signé".
     */
    public ResultatValidation validerEtEmbaucher(Integer contratCandidatId) {
        ContratCandidat cc = findById(contratCandidatId);
        exigerStatut(cc, SIGNE, "valider");
        changerStatut(cc, VALIDE);
        ResultatEmbauche embauche = embaucheService.embaucher(cc);
        return new ResultatValidation(cc, embauche);
    }

    // ------------------------------------------------------------ utilitaires privés
    private PropositionEmbauche verifierCandidature(Integer candidatId, Integer candidatureId) {
        if (candidatureId == null) throw new IllegalStateException("La création d’un contrat exige une candidature acceptée.");
        boolean retenue = contratCandidatRepo.findCandidaturesRetenues(candidatId).stream()
                .anyMatch(c -> c.getIdCandidature().equals(candidatureId));
        if (!retenue) {
            throw new IllegalArgumentException("Cette candidature n'est pas retenue pour ce candidat "
                    + "(proposition d'embauche sans accord final).");
        }
        return propositionRepo.findByCandidatureId(candidatureId)
                .orElseThrow(() -> new IllegalArgumentException("Aucune proposition n'est liée à cette candidature."));
    }

    private ContratCandidat lier(Integer candidatId, String type, ContratCe ce, ContratCdd cdd, ContratCdi cdi,
                                 Integer candidatureId) {
        return lier(candidatId, type, ce, cdd, cdi, candidatureId, null);
    }

    private ContratCandidat lier(Integer candidatId, String type, ContratCe ce, ContratCdd cdd, ContratCdi cdi,
                                 Integer candidatureId, PropositionEmbauche proposition) {
        Candidat candidat = candidatRepo.findById(candidatId)
                .orElseThrow(() -> new ResourceNotFoundException("Candidat introuvable : id=" + candidatId));
        ContratCandidat cc = new ContratCandidat();
        cc.setCandidat(candidat);
        cc.setTypeContrat(type);
        cc.setContratCe(ce);
        cc.setContratCdd(cdd);
        cc.setContratCdi(cdi);
        cc.setIdCandidature(candidatureId);
        cc.setProposition(proposition);
        cc = contratCandidatRepo.save(cc);
        historiser(cc, statut(CREE));
        return cc;
    }

    private PropositionEmbauche propositionAcceptee(Integer candidatId, Integer propositionId) {
        PropositionInfo info = propositionRepo.findInfo(propositionId).orElseThrow(() -> new ResourceNotFoundException("Proposition introuvable."));
        if (!candidatId.equals(info.getIdCandidat())) throw new SecurityException("Cette proposition ne vous appartient pas.");
        if (contratCandidatRepo.existsByPropositionId(propositionId)) throw new IllegalStateException("Un contrat est déjà lié à cette proposition.");
        PropositionEmbauche p = propositionRepo.findAccordFinal(propositionId).orElseThrow(() -> new IllegalStateException("La proposition doit être en accord final."));
        if (p.getSalaireFinal() == null) throw new IllegalStateException("La proposition ne contient pas de salaire final.");
        return p;
    }

    private void verifierTravailleur(Integer candidatId, Travailleur travailleur) {
        String email = candidatRepo.findById(candidatId).orElseThrow().getEmail();
        if (travailleur == null || travailleur.getId() == null || travailleurRepo.findById(travailleur.getId())
                .filter(t -> email.equalsIgnoreCase(t.getEmail())).isEmpty())
            throw new SecurityException("Aucun dossier travailleur ne correspond à votre compte candidat.");
    }

    private LocalDate date(String value) {
        return LocalDate.parse(value, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    private BigDecimal heures(String horaire) {
        if (horaire == null) return null;
        java.util.regex.Matcher m = java.util.regex.Pattern.compile("(\\d+(?:[.,]\\d+)?)\\s*(?:h|heures?)\\s*(?:/\\s*semaine|par\\s*semaine|hebdomadaires)", java.util.regex.Pattern.CASE_INSENSITIVE).matcher(horaire);
        if (!m.find()) return null;
        try { return new BigDecimal(m.group(1).replace(',', '.')); } catch (NumberFormatException e) { return null; }
    }

    private StatutContrat statut(String libelle) {
        return statutRepo.findByLibelle(libelle)
                .orElseThrow(() -> new IllegalStateException("Statut '" + libelle + "' absent de statut_contrat (jeu de données de test non chargé ?)"));
    }

    private StatutContrat getStatut(ContratCandidat cc) {
        return switch (cc.getTypeContrat()) {
            case "CE" -> cc.getContratCe().getStatut();
            case "CDD" -> cc.getContratCdd().getStatut();
            default -> cc.getContratCdi().getStatut();
        };
    }

    private void exigerStatut(ContratCandidat cc, String attendu, String action) {
        String actuel = getStatut(cc).getLibelle();
        if (!attendu.equals(actuel)) {
            throw new IllegalStateException("Impossible de " + action + " : le contrat est '" + actuel + "' (attendu : '" + attendu + "')");
        }
    }

    private void changerStatut(ContratCandidat cc, String libelle) {
        StatutContrat s = statut(libelle);
        switch (cc.getTypeContrat()) {
            case "CE" -> cc.getContratCe().setStatut(s);
            case "CDD" -> cc.getContratCdd().setStatut(s);
            default -> cc.getContratCdi().setStatut(s);
        }
        historiser(cc, s);
    }

    private void marquerDateSignature(ContratCandidat cc) {
        LocalDate today = LocalDate.now();
        switch (cc.getTypeContrat()) {
            case "CE" -> { if (cc.getContratCe().getDateSignature() == null) cc.getContratCe().setDateSignature(today); }
            case "CDD" -> { if (cc.getContratCdd().getDateConclusion() == null) cc.getContratCdd().setDateConclusion(today); }
            default -> { if (cc.getContratCdi().getDateSignature() == null) cc.getContratCdi().setDateSignature(today); }
        }
    }

    private void historiser(ContratCandidat cc, StatutContrat s) {
        HistoriqueStatutContrat h = new HistoriqueStatutContrat();
        h.setContratCandidat(cc);
        h.setStatutContrat(s);
        h.setDateChangement(LocalDateTime.now());
        historiqueRepo.save(h);
    }
}
