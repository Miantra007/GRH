package mg.grh.repository;

import mg.grh.model.ContratCandidat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ContratCandidatRepository extends JpaRepository<ContratCandidat, Integer> {
    List<ContratCandidat> findByCandidatId(Integer candidatId);
}
