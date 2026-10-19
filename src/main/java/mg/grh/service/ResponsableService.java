package mg.grh.service;

import mg.grh.model.Responsable;
import mg.grh.repository.ResponsableRepository;
import org.springframework.stereotype.Service;

@Service
public class ResponsableService extends GenericService<Responsable> {
    public ResponsableService(ResponsableRepository repository) {
        super(repository);
    }
}
