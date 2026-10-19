package mg.grh.service;

import mg.grh.model.StatutContrat;
import mg.grh.repository.StatutContratRepository;
import org.springframework.stereotype.Service;

@Service
public class StatutContratService extends GenericService<StatutContrat> {
    public StatutContratService(StatutContratRepository repository) {
        super(repository);
    }
}
