package mg.grh.repository;

import mg.grh.model.CentreInteret;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CentreInteretRepository extends JpaRepository<CentreInteret, Integer> {
}
