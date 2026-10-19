package mg.grh.service;

import mg.grh.model.Nationalite;
import mg.grh.repository.NationaliteRepository;
import org.springframework.stereotype.Service;

@Service
public class NationaliteService extends GenericService<Nationalite> {
    public NationaliteService(NationaliteRepository repository) {
        super(repository);
    }
}
