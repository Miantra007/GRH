package mg.grh.service;

import mg.grh.model.*;
import mg.grh.repository.BesoinPosteRepository;
import mg.grh.repository.CandidatureRepository;
import mg.grh.repository.NotificationRepository;
import mg.grh.repository.ResponsableRepository;
import mg.grh.repository.StatutCandidatureRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CandidatureService extends GenericService<Candidature> {
    /** Candidatures exigées en plus du nombre de postes avant d'ouvrir la sélection. */
    public static final int MARGE_CANDIDATURES = 1;

    private final CandidatureRepository candidatures;
    private final StatutCandidatureRepository statuts;
    private final BesoinPosteRepository besoinPostes;
    private final NotificationRepository notifications;
    private final ResponsableRepository responsables;
    public CandidatureService(CandidatureRepository candidatures, StatutCandidatureRepository statuts,
            BesoinPosteRepository besoinPostes, NotificationRepository notifications, ResponsableRepository responsables) {
        super(candidatures); this.candidatures = candidatures; this.statuts = statuts;
        this.besoinPostes = besoinPostes; this.notifications = notifications; this.responsables = responsables;
    }

    public int nombrePostes(OffreEmploi offre) {
        return besoinPostes.findById(offre.getBesoinPosteId()).map(BesoinPoste::getNombrePostes).orElse(1);
    }

    public int seuilCandidatures(OffreEmploi offre) {
        return nombrePostes(offre) + MARGE_CANDIDATURES;
    }

    public long nombreCandidatures(OffreEmploi offre) {
        return candidatures.countByOffreId(offre.getId());
    }

    /** Postes encore libres : nombre de postes moins les candidats déjà retenus (proposition non refusée). */
    public long postesRestants(OffreEmploi offre) {
        return Math.max(0, nombrePostes(offre) - candidatures.countRetenusActifs(offre.getId()));
    }

    public boolean selectionOuverte(OffreEmploi offre) {
        return nombreCandidatures(offre) >= seuilCandidatures(offre);
    }

    private void exigerSelectionOuverte(OffreEmploi offre) {
        long recues = nombreCandidatures(offre);
        int seuil = seuilCandidatures(offre);
        if (recues < seuil)
            throw new IllegalStateException("Sélection pas encore ouverte pour l’offre « " + offre.getTitre() + " » : "
                    + recues + " candidature(s) reçue(s) sur " + seuil + " requises ("
                    + nombrePostes(offre) + " poste(s) + " + MARGE_CANDIDATURES + ").");
    }

    private void notifierSelectionOuverte(OffreEmploi offre) {
        Besoin besoin = besoinPostes.findById(offre.getBesoinPosteId()).map(BesoinPoste::getBesoin).orElse(null);
        List<Responsable> destinataires = besoin != null && besoin.getRhTraitant() != null
                ? List.of(besoin.getRhTraitant())
                : responsables.findByRoleLibelle("Administrateur RH");
        String message = "L’offre « " + offre.getTitre() + " » a reçu " + seuilCandidatures(offre) + " candidatures pour "
                + nombrePostes(offre) + " poste(s) demandé(s). Vous pouvez maintenant étudier les dossiers et lancer les tests et entretiens.";
        destinataires.forEach(rh -> notifications.save(Notification.pour(null, rh.getId(), null,
                "Nombre de candidatures atteint", message)));
    }
    public List<Candidature> toutes() { return candidatures.findAll(); }
    public boolean dejaPostule(Integer offreId, Integer candidatId) {
        return candidatId != null && candidatures.existsByOffreIdAndCandidatId(offreId, candidatId);
    }
    @Transactional
    public Candidature deposer(OffreEmploi offre, Candidat candidat) {
        if (candidatures.existsByOffreIdAndCandidatId(offre.getId(), candidat.getId()))
            throw new IllegalArgumentException("Une candidature existe déjà pour cette offre et cette adresse email.");
        Candidature c = new Candidature();
        c.setOffre(offre); c.setCandidat(candidat); c.setDateCandidature(LocalDateTime.now());
        c.setStatut(statuts.findByLibelle("RECUE").orElseThrow(() -> new IllegalStateException("Le statut RECUE manque dans statut_candidature.")));
        try { c = candidatures.saveAndFlush(c); }
        catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Une candidature existe déjà pour cette offre et cette adresse email.", e);
        }
        if (nombreCandidatures(offre) == seuilCandidatures(offre)) notifierSelectionOuverte(offre);
        return c;
    }
    @Transactional
    public Candidature changerStatut(Integer id, String libelle) {
        Candidature c = candidatures.findById(id).orElseThrow(() -> new IllegalArgumentException("Candidature introuvable."));
        exigerSelectionOuverte(c.getOffre());
        String actuel = c.getStatut().getLibelle();
        java.util.Map<String, java.util.Set<String>> transitions = java.util.Map.of(
                "RECUE", java.util.Set.of("DOSSIER_RETENU", "DOSSIER_REJETE"),
                "DOSSIER_RETENU", java.util.Set.of("TEST_TECHNIQUE", "ENTRETIEN", "NON_RETENUE"),
                "DOSSIER_REJETE", java.util.Set.of(),
                "TEST_TECHNIQUE", java.util.Set.of("ENTRETIEN", "NON_RETENUE"),
                "ENTRETIEN", java.util.Set.of("RETENUE", "NON_RETENUE"),
                "RETENUE", java.util.Set.of("NON_RETENUE"),
                "NON_RETENUE", java.util.Set.of());
        if (!actuel.equals(libelle) && !transitions.getOrDefault(actuel, java.util.Set.of()).contains(libelle))
            throw new IllegalStateException("Transition de candidature interdite : " + actuel + " → " + libelle + ".");
        if ("RETENUE".equals(libelle) && !"RETENUE".equals(actuel) && postesRestants(c.getOffre()) == 0)
            throw new IllegalStateException("Tous les postes de l’offre « " + c.getOffre().getTitre()
                    + " » sont déjà attribués : impossible de retenir un autre candidat.");
        c.setStatut(statuts.findByLibelle(libelle).orElseThrow(() -> new IllegalArgumentException("Statut inconnu.")));
        return candidatures.save(c);
    }
}
