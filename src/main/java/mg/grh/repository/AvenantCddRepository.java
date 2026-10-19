package mg.grh.repository;

import mg.grh.model.AvenantCdd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvenantCddRepository extends JpaRepository<AvenantCdd, Integer> {
}
