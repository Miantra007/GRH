package mg.grh.service;

import mg.grh.model.Pays;
import mg.grh.repository.PaysRepository;
import org.springframework.stereotype.Service;

@Service
public class PaysService extends GenericService<Pays> {
    public PaysService(PaysRepository repository) {
        super(repository);
    }
}
