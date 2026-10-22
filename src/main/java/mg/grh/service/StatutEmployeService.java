package mg.grh.service;

import mg.grh.model.StatutEmploye;
import mg.grh.repository.StatutEmployeRepository;
import org.springframework.stereotype.Service;

@Service
public class StatutEmployeService extends GenericService<StatutEmploye> {
    public StatutEmployeService(StatutEmployeRepository repository) {
        super(repository);
    }
}
