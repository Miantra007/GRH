package mg.grh.service;

import mg.grh.model.Besoin;
import mg.grh.repository.BesoinRepository;
import org.springframework.stereotype.Service;

@Service
public class BesoinService extends GenericService<Besoin> {
    public BesoinService(BesoinRepository repository) {
        super(repository);
    }
}
