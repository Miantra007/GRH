package mg.grh.service;

import mg.grh.model.ExperienceProfessionnelle;
import mg.grh.repository.ExperienceProfessionnelleRepository;
import org.springframework.stereotype.Service;

@Service
public class ExperienceProfessionnelleService extends GenericService<ExperienceProfessionnelle> {
    public ExperienceProfessionnelleService(ExperienceProfessionnelleRepository repository) {
        super(repository);
    }
}
