package mg.grh.service;

import mg.grh.model.StatutPoste;
import mg.grh.repository.StatutPosteRepository;
import org.springframework.stereotype.Service;

@Service
public class StatutPosteService extends GenericService<StatutPoste> {
    public StatutPosteService(StatutPosteRepository repository) {
        super(repository);
    }
}
