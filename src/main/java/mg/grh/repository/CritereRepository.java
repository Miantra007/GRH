package mg.grh.repository;

import mg.grh.model.CritereProposition;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CritereRepository extends JpaRepository<CritereProposition, Integer> {
    List<CritereProposition> findByPropositionIdOrderByTypeIdAsc(Integer idProposition);
}
