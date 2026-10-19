package mg.grh.repository;

import mg.grh.model.ContratCdd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContratCddRepository extends JpaRepository<ContratCdd, Integer> {
}
