package mg.grh.controller;

import mg.grh.model.Candidat;
import mg.grh.service.CandidatService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/candidat")
public class CandidatController extends GenericController<Candidat> {
    public CandidatController(CandidatService service) {
        super(service);
    }
}
