package mg.grh.service;

import mg.grh.model.TypeConsigne;
import mg.grh.repository.TypeConsigneRepository;
import org.springframework.stereotype.Service;

@Service
public class TypeConsigneService extends GenericService<TypeConsigne> {
    public TypeConsigneService(TypeConsigneRepository repository) {
        super(repository);
    }
}
