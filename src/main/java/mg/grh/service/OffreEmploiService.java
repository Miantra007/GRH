package mg.grh.service;

import mg.grh.model.BesoinPoste;
import mg.grh.model.OffreEmploi;
import mg.grh.repository.BesoinPosteRepository;
import mg.grh.repository.OffreEmploiRepository;
import mg.grh.repository.StatutOffreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OffreEmploiService extends GenericService<OffreEmploi> {
    private final OffreEmploiRepository offreRepository;
    private final BesoinPosteRepository besoinPosteRepository;
    private final StatutOffreRepository statutOffreRepository;

    /** Une offre prête à publier, avec le poste du besoin (salaire retenu, nombre, direction). */
    public record OffreAPublier(OffreEmploi offre, BesoinPoste poste) {}

    public OffreEmploiService(OffreEmploiRepository repository, BesoinPosteRepository besoinPosteRepository,
            StatutOffreRepository statutOffreRepository) {
        super(repository);
        this.offreRepository = repository;
        this.besoinPosteRepository = besoinPosteRepository;
        this.statutOffreRepository = statutOffreRepository;
    }

    public List<OffreEmploi> offresPubliees() {
        return offreRepository.findOffresPubliees().stream().filter(o -> !pourvue(o)).toList();
    }
    public OffreEmploi offrePubliee(Integer id) {
        return offreRepository.findOffrePubliee(id).filter(o -> !pourvue(o))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cette offre n’est plus disponible."));
    }

    /** Tous les postes de l'offre sont occupés par des candidats dont le contrat est validé. */
    private boolean pourvue(OffreEmploi offre) {
        int postes = besoinPosteRepository.findById(offre.getBesoinPosteId())
                .map(bp -> bp.getNombrePostes()).orElse(1);
        return offreRepository.countEmbauchesValidees(offre.getId()) >= postes;
    }

    @Transactional(readOnly = true)
    public List<OffreAPublier> offresAPublier() {
        return offreRepository.findOffresAPublier().stream()
                .map(offre -> new OffreAPublier(offre, besoinPosteRepository.findById(offre.getBesoinPosteId()).orElse(null)))
                .toList();
    }

    @Transactional(readOnly = true)
    public long nombreOffresAPublier() { return offreRepository.findOffresAPublier().size(); }

    /** Publie une offre dont le poste a été validé par le DG : elle devient visible sur le site carrières. */
    @Transactional
    public OffreEmploi publier(Integer id) {
        OffreEmploi offre = offreRepository.findOffresAPublier().stream()
                .filter(o -> o.getId().equals(id)).findFirst()
                .orElseThrow(() -> new IllegalStateException("Cette offre n'est pas en attente de publication."));
        if (offre.getDateLimite() != null && offre.getDateLimite().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("La date limite de l'offre « " + offre.getTitre() + " » est dépassée.");
        }
        offre.setStatutOffre(statutOffreRepository.findByLibelle("PUBLIEE")
                .orElseThrow(() -> new IllegalStateException("Le statut PUBLIEE doit être initialisé.")));
        offre.setVisibleClient(true);
        offre.setDatePublication(LocalDateTime.now());
        return offreRepository.save(offre);
    }
}
