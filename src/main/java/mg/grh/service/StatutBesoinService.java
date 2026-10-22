package mg.grh.service;

import mg.grh.model.StatutBesoin;
import mg.grh.repository.StatutBesoinRepository;
import org.springframework.stereotype.Service;

@Service
public class StatutBesoinService extends GenericService<StatutBesoin> {
    public StatutBesoinService(StatutBesoinRepository repository) {
        super(repository);
    }
}
