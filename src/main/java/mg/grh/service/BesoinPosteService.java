package mg.grh.service;

import mg.grh.model.BesoinPoste;
import mg.grh.repository.BesoinPosteRepository;
import org.springframework.stereotype.Service;

@Service
public class BesoinPosteService extends GenericService<BesoinPoste> {
    public BesoinPosteService(BesoinPosteRepository repository) {
        super(repository);
    }
}
