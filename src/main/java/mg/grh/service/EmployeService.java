package mg.grh.service;

import mg.grh.model.Employe;
import mg.grh.repository.EmployeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmployeService extends GenericService<Employe> {

    private final EmployeRepository employeRepository;

    public EmployeService(EmployeRepository repository) {
        super(repository);
        this.employeRepository = repository;
    }

    /** Employés qui occupent un poste (les plus récents d'abord). */
    @Transactional(readOnly = true)
    public List<Employe> enPoste() {
        return employeRepository.findByPosteFilleIsNotNullOrderByIdDesc();
    }

    /** Tous les employés, y compris ceux qui ont quitté leur poste (remplacés, suspendus). */
    @Transactional(readOnly = true)
    public List<Employe> tous() {
        return employeRepository.findAllByOrderByIdDesc();
    }
}
