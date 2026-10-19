package mg.grh.repository;

import mg.grh.model.HistoriqueStatutContrat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HistoriqueStatutContratRepository extends JpaRepository<HistoriqueStatutContrat, Integer> {
    List<HistoriqueStatutContrat> findByContratCandidatIdOrderByDateChangementAsc(Integer id);
}
