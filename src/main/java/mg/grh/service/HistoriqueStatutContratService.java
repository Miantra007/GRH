package mg.grh.service;

import mg.grh.model.HistoriqueStatutContrat;
import mg.grh.repository.HistoriqueStatutContratRepository;
import org.springframework.stereotype.Service;

@Service
public class HistoriqueStatutContratService extends GenericService<HistoriqueStatutContrat> {
    public HistoriqueStatutContratService(HistoriqueStatutContratRepository repository) {
        super(repository);
    }
}
