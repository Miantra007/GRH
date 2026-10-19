package mg.grh.service;

import mg.grh.model.Travailleur;
import mg.grh.repository.TravailleurRepository;
import org.springframework.stereotype.Service;

@Service
public class TravailleurService extends GenericService<Travailleur> {
    public TravailleurService(TravailleurRepository repository) {
        super(repository);
    }
}
