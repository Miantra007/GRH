package mg.grh.service;

import mg.grh.model.CvLangue;
import mg.grh.repository.CvLangueRepository;
import org.springframework.stereotype.Service;

@Service
public class CvLangueService extends GenericService<CvLangue> {
    public CvLangueService(CvLangueRepository repository) {
        super(repository);
    }
}
