package mg.grh.service;

import mg.grh.model.PosteFille;
import mg.grh.repository.PosteFilleRepository;
import org.springframework.stereotype.Service;

@Service
public class PosteFilleService extends GenericService<PosteFille> {
    public PosteFilleService(PosteFilleRepository repository) {
        super(repository);
    }
}
