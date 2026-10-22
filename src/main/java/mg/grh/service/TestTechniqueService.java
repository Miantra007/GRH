package mg.grh.service;

import mg.grh.model.TestTechnique;
import mg.grh.repository.TestTechniqueRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TestTechniqueService extends GenericService<TestTechnique> {
    private final TestTechniqueRepository repository;
    public TestTechniqueService(TestTechniqueRepository repository) {
        super(repository);
        this.repository = repository;
    }
    public List<TestTechnique> pourCandidature(Integer candidatureId) {
        return repository.findByCandidatureIdOrderByDateTestAsc(candidatureId);
    }
}
