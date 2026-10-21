package mg.grh.repository;

import mg.grh.model.DecisionRh;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DecisionRhRepository extends JpaRepository<DecisionRh, Integer> {
    Optional<DecisionRh> findByLibelle(String libelle);
}
