package mg.grh.service;

import mg.grh.model.Poste;
import mg.grh.repository.PosteRepository;
import org.springframework.stereotype.Service;

@Service
public class PosteService extends GenericService<Poste> {
    public PosteService(PosteRepository repository) {
        super(repository);
    }
}
