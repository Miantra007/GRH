package mg.grh.service;

import mg.grh.model.SituationFamille;
import mg.grh.repository.SituationFamilleRepository;
import org.springframework.stereotype.Service;

@Service
public class SituationFamilleService extends GenericService<SituationFamille> {
    public SituationFamilleService(SituationFamilleRepository repository) {
        super(repository);
    }
}
