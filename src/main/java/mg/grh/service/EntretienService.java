package mg.grh.service;

import mg.grh.model.Entretien;
import mg.grh.repository.EntretienRepository;
import org.springframework.stereotype.Service;

@Service
public class EntretienService extends GenericService<Entretien> {
    public EntretienService(EntretienRepository repository) {
        super(repository);
    }
}
