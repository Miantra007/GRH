package mg.grh.service;

import mg.grh.model.TexteLegal;
import mg.grh.repository.TexteLegalRepository;
import org.springframework.stereotype.Service;

@Service
public class TexteLegalService extends GenericService<TexteLegal> {
    public TexteLegalService(TexteLegalRepository repository) {
        super(repository);
    }
}
