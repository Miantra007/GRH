package mg.grh.service;

import mg.grh.model.*;
import mg.grh.repository.CandidatureRepository;
import mg.grh.repository.StatutCandidatureRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HexFormat;
import java.util.List;

@Service
public class CandidatureService extends GenericService<Candidature> {
    private final CandidatureRepository candidatures;
    private final StatutCandidatureRepository statuts;
    private final SecureRandom random = new SecureRandom();
    public CandidatureService(CandidatureRepository candidatures, StatutCandidatureRepository statuts) {
        super(candidatures); this.candidatures = candidatures; this.statuts = statuts;
    }
    public List<Candidature> toutes() { return candidatures.findAll(); }
    public boolean dejaPostule(Integer offreId, Integer candidatId) {
        return candidatId != null && candidatures.existsByOffreIdAndCandidatId(offreId, candidatId);
    }
    public Candidature parCodeSuivi(String code) {
        if (code == null || !code.matches("[0-9a-f]{64}")) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Code de suivi invalide.");
        return candidatures.findByCodeSuivi(code).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Code de suivi invalide."));
    }
    @Transactional
    public Candidature deposer(OffreEmploi offre, Candidat candidat) {
        if (candidatures.existsByOffreIdAndCandidatId(offre.getId(), candidat.getId()))
            throw new IllegalArgumentException("Une candidature existe déjà pour cette offre et cette adresse email.");
        Candidature c = new Candidature();
        c.setOffre(offre); c.setCandidat(candidat); c.setDateCandidature(LocalDateTime.now());
        c.setStatut(statuts.findByLibelle("RECUE").orElseThrow(() -> new IllegalStateException("Le statut RECUE manque dans statut_candidature.")));
        byte[] token = new byte[32]; random.nextBytes(token); c.setCodeSuivi(HexFormat.of().formatHex(token));
        try { return candidatures.saveAndFlush(c); }
        catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Une candidature existe déjà pour cette offre et cette adresse email.", e);
        }
    }
    @Transactional
    public Candidature changerStatut(Integer id, String libelle) {
        Candidature c = candidatures.findById(id).orElseThrow(() -> new IllegalArgumentException("Candidature introuvable."));
        String actuel = c.getStatut().getLibelle();
        java.util.Map<String, java.util.Set<String>> transitions = java.util.Map.of(
                "RECUE", java.util.Set.of("DOSSIER_RETENU", "DOSSIER_REJETE"),
                "DOSSIER_RETENU", java.util.Set.of("TEST_TECHNIQUE", "ENTRETIEN", "NON_RETENUE"),
                "DOSSIER_REJETE", java.util.Set.of("NON_RETENUE"),
                "TEST_TECHNIQUE", java.util.Set.of("ENTRETIEN", "NON_RETENUE"),
                "ENTRETIEN", java.util.Set.of("RETENUE", "NON_RETENUE"),
                "RETENUE", java.util.Set.of("NON_RETENUE"),
                "NON_RETENUE", java.util.Set.of());
        if (!actuel.equals(libelle) && !transitions.getOrDefault(actuel, java.util.Set.of()).contains(libelle))
            throw new IllegalStateException("Transition de candidature interdite : " + actuel + " → " + libelle + ".");
        c.setStatut(statuts.findByLibelle(libelle).orElseThrow(() -> new IllegalArgumentException("Statut inconnu.")));
        return candidatures.save(c);
    }
}
