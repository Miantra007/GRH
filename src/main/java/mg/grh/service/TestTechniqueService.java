package mg.grh.service;

import mg.grh.model.TestTechnique;
import mg.grh.repository.TestTechniqueRepository;
import org.springframework.stereotype.Service;

@Service
public class TestTechniqueService extends GenericService<TestTechnique> {
    public TestTechniqueService(TestTechniqueRepository repository) {
        super(repository);
    }
}
