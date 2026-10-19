package mg.grh.service;

import mg.grh.model.Direction;
import mg.grh.repository.DirectionRepository;
import org.springframework.stereotype.Service;

@Service
public class DirectionService extends GenericService<Direction> {
    public DirectionService(DirectionRepository repository) {
        super(repository);
    }
}
