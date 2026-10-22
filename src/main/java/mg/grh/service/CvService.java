package mg.grh.service;

import mg.grh.model.Cv;
import mg.grh.repository.CvRepository;
import org.springframework.stereotype.Service;

@Service
public class CvService extends GenericService<Cv> {
    public CvService(CvRepository repository) {
        super(repository);
    }

    public Cv enregistrer(Cv cv) {
        return repository.save(cv);
    }
}
