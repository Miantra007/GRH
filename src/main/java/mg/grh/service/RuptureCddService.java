package mg.grh.service;

import mg.grh.model.RuptureCdd;
import mg.grh.repository.RuptureCddRepository;
import org.springframework.stereotype.Service;

@Service
public class RuptureCddService extends GenericService<RuptureCdd> {
    public RuptureCddService(RuptureCddRepository repository) {
        super(repository);
    }
}
