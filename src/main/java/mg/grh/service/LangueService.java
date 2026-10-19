package mg.grh.service;

import mg.grh.model.Langue;
import mg.grh.repository.LangueRepository;
import org.springframework.stereotype.Service;

@Service
public class LangueService extends GenericService<Langue> {
    public LangueService(LangueRepository repository) {
        super(repository);
    }
}
