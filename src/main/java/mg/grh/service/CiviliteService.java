package mg.grh.service;

import mg.grh.model.Civilite;
import mg.grh.repository.CiviliteRepository;
import org.springframework.stereotype.Service;

@Service
public class CiviliteService extends GenericService<Civilite> {
    public CiviliteService(CiviliteRepository repository) {
        super(repository);
    }
}
