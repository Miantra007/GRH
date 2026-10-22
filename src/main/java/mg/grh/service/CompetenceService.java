package mg.grh.service;

import mg.grh.model.Competence;
import mg.grh.repository.CompetenceRepository;
import org.springframework.stereotype.Service;

@Service
public class CompetenceService extends GenericService<Competence> {
    public CompetenceService(CompetenceRepository repository) {
        super(repository);
    }

    public Competence enregistrer(Competence competence) {
        return repository.save(competence);
    }
}
