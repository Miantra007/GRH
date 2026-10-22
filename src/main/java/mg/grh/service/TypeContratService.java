package mg.grh.service;

import mg.grh.model.TypeContrat;
import mg.grh.repository.TypeContratRepository;
import org.springframework.stereotype.Service;

@Service
public class TypeContratService extends GenericService<TypeContrat> {
    public TypeContratService(TypeContratRepository repository) {
        super(repository);
    }
}
