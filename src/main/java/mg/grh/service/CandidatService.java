package mg.grh.service;

import mg.grh.model.Candidat;
import mg.grh.repository.CandidatRepository;
import org.springframework.stereotype.Service;

@Service
public class CandidatService extends GenericService<Candidat> {
    public CandidatService(CandidatRepository repository) {
        super(repository);
    }
}
