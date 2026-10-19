package mg.grh.repository;

import mg.grh.model.ContratCe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContratCeRepository extends JpaRepository<ContratCe, Integer> {
}
