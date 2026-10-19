package mg.grh.service;

import mg.grh.model.Education;
import mg.grh.repository.EducationRepository;
import org.springframework.stereotype.Service;

@Service
public class EducationService extends GenericService<Education> {
    public EducationService(EducationRepository repository) {
        super(repository);
    }
}
