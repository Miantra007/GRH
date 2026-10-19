package mg.grh.service;

import mg.grh.model.ContratCdi;
import mg.grh.repository.ContratCdiRepository;
import org.springframework.stereotype.Service;

@Service
public class ContratCdiService extends GenericService<ContratCdi> {
    public ContratCdiService(ContratCdiRepository repository) {
        super(repository);
    }
}
