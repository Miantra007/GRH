package mg.grh.service;

import mg.grh.model.Entretien;
import mg.grh.repository.EntretienRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EntretienService extends GenericService<Entretien> {
    private final EntretienRepository repository;
    public EntretienService(EntretienRepository repository) {
        super(repository);
        this.repository = repository;
    }
    public List<Entretien> pourCandidature(Integer candidatureId) {
        return repository.findByCandidatureIdOrderByDateEntretienAsc(candidatureId);
    }
}
