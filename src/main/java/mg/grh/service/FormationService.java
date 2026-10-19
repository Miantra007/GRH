package mg.grh.service;

import mg.grh.model.Formation;
import mg.grh.repository.FormationRepository;
import org.springframework.stereotype.Service;

@Service
public class FormationService extends GenericService<Formation> {
    public FormationService(FormationRepository repository) {
        super(repository);
    }
}
