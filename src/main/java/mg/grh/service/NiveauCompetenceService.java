package mg.grh.service;

import mg.grh.model.NiveauCompetence;
import mg.grh.repository.NiveauCompetenceRepository;
import org.springframework.stereotype.Service;

@Service
public class NiveauCompetenceService extends GenericService<NiveauCompetence> {
    public NiveauCompetenceService(NiveauCompetenceRepository repository) {
        super(repository);
    }
}
