package mg.grh.repository;

import mg.grh.model.CvLangue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CvLangueRepository extends JpaRepository<CvLangue, Integer> {
    List<CvLangue> findByCvId(Integer cvId);
    void deleteByCvId(Integer cvId);
}
