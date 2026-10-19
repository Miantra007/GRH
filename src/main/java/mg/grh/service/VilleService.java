package mg.grh.service;

import mg.grh.model.Ville;
import mg.grh.repository.VilleRepository;
import org.springframework.stereotype.Service;

@Service
public class VilleService extends GenericService<Ville> {
    public VilleService(VilleRepository repository) {
        super(repository);
    }
}
