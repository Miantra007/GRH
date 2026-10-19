package mg.grh.repository;

import mg.grh.model.ContratCdi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContratCdiRepository extends JpaRepository<ContratCdi, Integer> {
}
