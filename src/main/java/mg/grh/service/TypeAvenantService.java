package mg.grh.service;

import mg.grh.model.TypeAvenant;
import mg.grh.repository.TypeAvenantRepository;
import org.springframework.stereotype.Service;

@Service
public class TypeAvenantService extends GenericService<TypeAvenant> {
    public TypeAvenantService(TypeAvenantRepository repository) {
        super(repository);
    }
}
