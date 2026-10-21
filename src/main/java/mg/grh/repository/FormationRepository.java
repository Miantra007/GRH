package mg.grh.repository;

import mg.grh.model.Formation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FormationRepository extends JpaRepository<Formation, Integer> {
    List<Formation> findByCvId(Integer cvId);
    void deleteByCvId(Integer cvId);
}
