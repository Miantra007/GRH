package mg.grh.service;

import mg.grh.model.CategorieProfessionnelle;
import mg.grh.repository.CategorieProfessionnelleRepository;
import org.springframework.stereotype.Service;

@Service
public class CategorieProfessionnelleService extends GenericService<CategorieProfessionnelle> {
    public CategorieProfessionnelleService(CategorieProfessionnelleRepository repository) {
        super(repository);
    }
}
