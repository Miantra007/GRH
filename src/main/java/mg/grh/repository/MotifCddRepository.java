package mg.grh.repository;

import mg.grh.model.MotifCdd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MotifCddRepository extends JpaRepository<MotifCdd, Integer> {
}
