package mg.grh.service;

import mg.grh.model.MotifCdd;
import mg.grh.repository.MotifCddRepository;
import org.springframework.stereotype.Service;

@Service
public class MotifCddService extends GenericService<MotifCdd> {
    public MotifCddService(MotifCddRepository repository) {
        super(repository);
    }
}
