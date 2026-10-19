package mg.grh.service;

import mg.grh.model.AvenantCdd;
import mg.grh.repository.AvenantCddRepository;
import org.springframework.stereotype.Service;

@Service
public class AvenantCddService extends GenericService<AvenantCdd> {
    public AvenantCddService(AvenantCddRepository repository) {
        super(repository);
    }
}
