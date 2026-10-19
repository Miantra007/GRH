package mg.grh.service;

import mg.grh.model.ConventionCollective;
import mg.grh.repository.ConventionCollectiveRepository;
import org.springframework.stereotype.Service;

@Service
public class ConventionCollectiveService extends GenericService<ConventionCollective> {
    public ConventionCollectiveService(ConventionCollectiveRepository repository) {
        super(repository);
    }
}
