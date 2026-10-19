package mg.grh.service;

import mg.grh.model.Employe;
import mg.grh.repository.EmployeRepository;
import org.springframework.stereotype.Service;

@Service
public class EmployeService extends GenericService<Employe> {
    public EmployeService(EmployeRepository repository) {
        super(repository);
    }
}
