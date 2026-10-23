package mg.grh.service;

import mg.grh.model.Sexe;
import mg.grh.repository.SexeRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SexeService {
    private final SexeRepository repo;

    public SexeService(SexeRepository repo) {
        this.repo = repo;
    }

    public List<Sexe> findAll() {
        return repo.findAll();
    }

    public Sexe findById(Integer id) {
        return repo.findById(id).orElse(null);
    }
}
