package mg.grh.service;

import mg.grh.model.NiveauLangue;
import mg.grh.repository.NiveauLangueRepository;
import org.springframework.stereotype.Service;

@Service
public class NiveauLangueService extends GenericService<NiveauLangue> {
    public NiveauLangueService(NiveauLangueRepository repository) {
        super(repository);
    }
}
