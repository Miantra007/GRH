package mg.grh.service;

import mg.grh.model.Employeur;
import mg.grh.repository.EmployeurRepository;
import org.springframework.stereotype.Service;

@Service
public class EmployeurService extends GenericService<Employeur> {
    public EmployeurService(EmployeurRepository repository) {
        super(repository);
    }
}
