package mg.grh.service;

import mg.grh.model.OffreEmploi;
import mg.grh.repository.OffreEmploiRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.util.List;

@Service
public class OffreEmploiService extends GenericService<OffreEmploi> {
    private final OffreEmploiRepository offreRepository;
    public OffreEmploiService(OffreEmploiRepository repository) { super(repository); this.offreRepository = repository; }
    public List<OffreEmploi> offresPubliees() { return offreRepository.findOffresPubliees(); }
    public OffreEmploi offrePubliee(Integer id) {
        return offreRepository.findOffrePubliee(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cette offre n’est plus disponible."));
    }
}
