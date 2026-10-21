package mg.grh.repository;

import mg.grh.model.Competence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CompetenceRepository extends JpaRepository<Competence, Integer> {
    List<Competence> findByCvId(Integer cvId);
    void deleteByCvId(Integer cvId);
}
