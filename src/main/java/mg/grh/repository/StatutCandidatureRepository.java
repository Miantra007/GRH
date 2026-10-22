package mg.grh.repository;

import mg.grh.model.StatutCandidature;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface StatutCandidatureRepository extends JpaRepository<StatutCandidature, Integer> {
    Optional<StatutCandidature> findByLibelle(String libelle);
}
