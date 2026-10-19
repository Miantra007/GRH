package mg.grh.service;

import mg.grh.model.TypeRupture;
import mg.grh.repository.TypeRuptureRepository;
import org.springframework.stereotype.Service;

@Service
public class TypeRuptureService extends GenericService<TypeRupture> {
    public TypeRuptureService(TypeRuptureRepository repository) {
        super(repository);
    }
}
