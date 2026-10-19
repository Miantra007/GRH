package mg.grh.service;

import mg.grh.model.HistoriqueContratEmploye;
import mg.grh.repository.HistoriqueContratEmployeRepository;
import org.springframework.stereotype.Service;

@Service
public class HistoriqueContratEmployeService extends GenericService<HistoriqueContratEmploye> {
    public HistoriqueContratEmployeService(HistoriqueContratEmployeRepository repository) {
        super(repository);
    }
}
