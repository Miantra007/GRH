package mg.grh.repository;

import mg.grh.model.DossierCandidature;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DossierCandidatureRepository extends JpaRepository<DossierCandidature, Integer> {
    Optional<DossierCandidature> findByCandidatureId(Integer candidatureId);
}
