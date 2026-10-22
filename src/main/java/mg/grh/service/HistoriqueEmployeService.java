package mg.grh.service;

import mg.grh.model.HistoriqueEmploye;
import mg.grh.repository.HistoriqueEmployeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class HistoriqueEmployeService extends GenericService<HistoriqueEmploye> {

    private final HistoriqueEmployeRepository historiqueRepository;

    public HistoriqueEmployeService(HistoriqueEmployeRepository repository) {
        super(repository);
        this.historiqueRepository = repository;
    }

    /** Du plus récent au plus ancien. */
    @Transactional(readOnly = true)
    public List<HistoriqueEmploye> recents() {
        return historiqueRepository.findAllByOrderByDateDebutDescIdDesc();
    }
}
