package mg.grh.repository;

import mg.grh.model.Education;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EducationRepository extends JpaRepository<Education, Integer> {
    List<Education> findByCvId(Integer cvId);
    void deleteByCvId(Integer cvId);
}
