package mg.grh.service;

import mg.grh.model.ElementSalaireCdd;
import mg.grh.repository.ElementSalaireCddRepository;
import org.springframework.stereotype.Service;

@Service
public class ElementSalaireCddService extends GenericService<ElementSalaireCdd> {
    public ElementSalaireCddService(ElementSalaireCddRepository repository) {
        super(repository);
    }
}
