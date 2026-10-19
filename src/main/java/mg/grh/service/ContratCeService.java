package mg.grh.service;

import mg.grh.model.ContratCe;
import mg.grh.repository.ContratCeRepository;
import org.springframework.stereotype.Service;

@Service
public class ContratCeService extends GenericService<ContratCe> {
    public ContratCeService(ContratCeRepository repository) {
        super(repository);
    }
}
