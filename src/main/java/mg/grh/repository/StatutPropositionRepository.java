package mg.grh.repository;

import mg.grh.model.StatutProposition;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface StatutPropositionRepository extends JpaRepository<StatutProposition, Integer> {
    Optional<StatutProposition> findByLibelle(String libelle);
}