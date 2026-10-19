package mg.grh.service;

import mg.grh.model.CentreInteret;
import mg.grh.repository.CentreInteretRepository;
import org.springframework.stereotype.Service;

@Service
public class CentreInteretService extends GenericService<CentreInteret> {
    public CentreInteretService(CentreInteretRepository repository) {
        super(repository);
    }
}
