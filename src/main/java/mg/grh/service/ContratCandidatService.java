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

/**
 * Cycle de vie d'un contrat :
 *   (RH)               création  -> statut "Créé"   (CE par défaut, ou CDD / CDI)
 *   (Candidat)         signature -> statut "Signé"
 *   (Direction gén.)   validation-> statut "Validé"
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

    public ContratCandidatService(ContratCandidatRepository contratCandidatRepo, ContratCeRepository ceRepo,
                                  ContratCddRepository cddRepo, ContratCdiRepository cdiRepo,
                                  CandidatRepository candidatRepo, StatutContratRepository statutRepo,
                                  HistoriqueStatutContratRepository historiqueRepo, EmbaucheService embaucheService) {
        this.contratCandidatRepo = contratCandidatRepo;
        this.ceRepo = ceRepo;
        this.cddRepo = cddRepo;
        this.cdiRepo = cdiRepo;
        this.candidatRepo = candidatRepo;
        this.statutRepo = statutRepo;
        this.historiqueRepo = historiqueRepo;
        this.embaucheService = embaucheService;
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

    /** Contrats signés par le candidat, en attente de validation par la Direction générale. */
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
        verifierCandidature(candidatId, candidatureId);
        ce.setId(null);
        ce.setStatut(statut(CREE));
        return lier(candidatId, "CE", ceRepo.save(ce), null, null, candidatureId);
    }

    public ContratCandidat creerCdd(Integer candidatId, ContratCdd cdd, Integer candidatureId) {
        verifierCandidature(candidatId, candidatureId);
        cdd.setId(null);
        cdd.setStatut(statut(CREE));
        return lier(candidatId, "CDD", null, cddRepo.save(cdd), null, candidatureId);
    }

    public ContratCandidat creerCdi(Integer candidatId, ContratCdi cdi, Integer candidatureId) {
        verifierCandidature(candidatId, candidatureId);
        cdi.setId(null);
        cdi.setStatut(statut(CREE));
        return lier(candidatId, "CDI", null, null, cdiRepo.save(cdi), candidatureId);
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
        return cc;
    }

    // ------------------------------------------------------------ 3) validation par la Direction générale (statut Validé)
    public ContratCandidat valider(Integer contratCandidatId) {
        return validerEtEmbaucher(contratCandidatId).contrat();
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
    private void verifierCandidature(Integer candidatId, Integer candidatureId) {
        if (candidatureId == null) return;
        boolean retenue = contratCandidatRepo.findCandidaturesRetenues(candidatId).stream()
                .anyMatch(c -> c.getIdCandidature().equals(candidatureId));
        if (!retenue) {
            throw new IllegalArgumentException("Cette candidature n'est pas retenue pour ce candidat "
                    + "(proposition d'embauche sans accord final).");
        }
    }

    private ContratCandidat lier(Integer candidatId, String type, ContratCe ce, ContratCdd cdd, ContratCdi cdi,
                                 Integer candidatureId) {
        Candidat candidat = candidatRepo.findById(candidatId)
                .orElseThrow(() -> new ResourceNotFoundException("Candidat introuvable : id=" + candidatId));
        ContratCandidat cc = new ContratCandidat();
        cc.setCandidat(candidat);
        cc.setTypeContrat(type);
        cc.setContratCe(ce);
        cc.setContratCdd(cdd);
        cc.setContratCdi(cdi);
        cc.setIdCandidature(candidatureId);
        cc = contratCandidatRepo.save(cc);
        historiser(cc, statut(CREE));
        return cc;
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
