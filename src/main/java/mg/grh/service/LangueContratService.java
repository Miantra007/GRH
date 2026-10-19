package mg.grh.service;

import mg.grh.model.LangueContrat;
import mg.grh.repository.LangueContratRepository;
import org.springframework.stereotype.Service;

@Service
public class LangueContratService extends GenericService<LangueContrat> {
    public LangueContratService(LangueContratRepository repository) {
        super(repository);
    }
}
