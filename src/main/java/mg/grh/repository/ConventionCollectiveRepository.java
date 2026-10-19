package mg.grh.repository;

import mg.grh.model.ConventionCollective;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConventionCollectiveRepository extends JpaRepository<ConventionCollective, Integer> {
}
