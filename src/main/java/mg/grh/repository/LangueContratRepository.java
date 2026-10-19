package mg.grh.repository;

import mg.grh.model.LangueContrat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LangueContratRepository extends JpaRepository<LangueContrat, Integer> {
}
