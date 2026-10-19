package mg.grh.service;

import mg.grh.model.Departement;
import mg.grh.repository.DepartementRepository;
import org.springframework.stereotype.Service;

@Service
public class DepartementService extends GenericService<Departement> {
    public DepartementService(DepartementRepository repository) {
        super(repository);
    }
}
