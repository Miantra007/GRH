package mg.grh.repository;

import mg.grh.model.CvLangue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CvLangueRepository extends JpaRepository<CvLangue, Integer> {
}
