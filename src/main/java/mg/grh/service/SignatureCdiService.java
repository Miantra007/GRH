package mg.grh.service;

import mg.grh.model.SignatureCdi;
import mg.grh.repository.SignatureCdiRepository;
import org.springframework.stereotype.Service;

@Service
public class SignatureCdiService extends GenericService<SignatureCdi> {
    public SignatureCdiService(SignatureCdiRepository repository) {
        super(repository);
    }
}
