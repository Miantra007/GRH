package mg.grh.repository;

import mg.grh.model.TestTechnique;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TestTechniqueRepository extends JpaRepository<TestTechnique, Integer> {
    List<TestTechnique> findByCandidatureIdOrderByDateTestAsc(Integer candidatureId);
}
