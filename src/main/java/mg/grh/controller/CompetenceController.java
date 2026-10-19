package mg.grh.controller;

import mg.grh.model.Competence;
import mg.grh.service.CompetenceService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/competence")
public class CompetenceController extends GenericController<Competence> {
    public CompetenceController(CompetenceService service) {
        super(service);
    }
}
