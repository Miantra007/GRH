package mg.grh.service;

import mg.grh.exception.ResourceNotFoundException;
import mg.grh.model.*;
import mg.grh.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Consignes d'intégration :
 *   (RH)       envoie au candidat ce qu'il doit faire : formation à suivre, compte bancaire à créer, matériel...
 *   (Candidat) consulte ses consignes et les marque comme faites (ou les rouvre)
 */
@Service
@Transactional
public class ConsigneIntegrationService extends GenericService<ConsigneIntegration> {

    private final ConsigneIntegrationRepository consigneRepo;
    private final CandidatRepository candidatRepo;
    private final ResponsableRepository responsableRepo;
    private final TypeConsigneRepository typeRepo;
    private final ContratCandidatRepository contratCandidatRepo;

    public ConsigneIntegrationService(ConsigneIntegrationRepository consigneRepo, CandidatRepository candidatRepo,
                                      ResponsableRepository responsableRepo, TypeConsigneRepository typeRepo,
                                      ContratCandidatRepository contratCandidatRepo) {
        super(consigneRepo);
        this.consigneRepo = consigneRepo;
        this.candidatRepo = candidatRepo;
        this.responsableRepo = responsableRepo;
        this.typeRepo = typeRepo;
        this.contratCandidatRepo = contratCandidatRepo;
    }

    // ------------------------------------------------------------ lecture
    @Transactional(readOnly = true)
    public List<ConsigneIntegration> findByCandidat(Integer candidatId) {
        return consigneRepo.findByCandidatIdOrderByDateEnvoiDesc(candidatId);
    }

    // ------------------------------------------------------------ RH : envoi
    public ConsigneIntegration envoyer(Integer candidatId, Integer rhId, Integer typeId, Integer contratCandidatId,
                                       String titre, String description, LocalDate dateLimite) {
        if (titre == null || titre.isBlank()) {
            throw new IllegalArgumentException("Le titre de la consigne est obligatoire.");
        }
        Candidat candidat = candidatRepo.findById(candidatId)
                .orElseThrow(() -> new ResourceNotFoundException("Candidat introuvable : id=" + candidatId));
        TypeConsigne type = typeRepo.findById(typeId)
                .orElseThrow(() -> new IllegalArgumentException("Type de consigne inconnu : id=" + typeId));

        ConsigneIntegration c = new ConsigneIntegration();
        c.setCandidat(candidat);
        c.setTypeConsigne(type);
        c.setTitre(titre.trim());
        c.setDescription(description == null || description.isBlank() ? null : description.trim());
        c.setDateLimite(dateLimite);
        c.setRhEmetteur(responsableRepo.findById(rhId).orElse(null));
        c.setDateEnvoi(LocalDateTime.now());

        if (contratCandidatId != null) {
            ContratCandidat cc = contratCandidatRepo.findById(contratCandidatId)
                    .orElseThrow(() -> new ResourceNotFoundException("Contrat introuvable : id=" + contratCandidatId));
            if (!cc.getCandidat().getId().equals(candidatId)) {
                throw new IllegalArgumentException("Ce contrat n'appartient pas à ce candidat.");
            }
            c.setContratCandidat(cc);
        }
        return consigneRepo.save(c);
    }

    // ------------------------------------------------------------ candidat : suivi
    public ConsigneIntegration marquerFaite(Integer consigneId, Integer candidatId, String commentaire) {
        ConsigneIntegration c = duCandidat(consigneId, candidatId);
        c.setDateRealisation(LocalDateTime.now());
        c.setCommentaireCandidat(commentaire == null || commentaire.isBlank() ? null : commentaire.trim());
        return c;
    }

    public ConsigneIntegration rouvrir(Integer consigneId, Integer candidatId) {
        ConsigneIntegration c = duCandidat(consigneId, candidatId);
        c.setDateRealisation(null);
        return c;
    }

    private ConsigneIntegration duCandidat(Integer consigneId, Integer candidatId) {
        ConsigneIntegration c = findById(consigneId);
        if (!c.getCandidat().getId().equals(candidatId)) {
            throw new SecurityException("Cette consigne ne vous est pas destinée.");
        }
        return c;
    }
}
