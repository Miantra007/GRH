package mg.grh.service;

import mg.grh.model.ContratCdd;
import mg.grh.repository.ContratCddRepository;
import org.springframework.stereotype.Service;

@Service
public class ContratCddService extends GenericService<ContratCdd> {
    public ContratCddService(ContratCddRepository repository) {
        super(repository);
    }
}
