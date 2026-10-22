package mg.grh.service;

import mg.grh.model.TypeBesoin;
import mg.grh.repository.TypeBesoinRepository;
import org.springframework.stereotype.Service;

@Service
public class TypeBesoinService extends GenericService<TypeBesoin> {
    public TypeBesoinService(TypeBesoinRepository repository) {
        super(repository);
    }
}
