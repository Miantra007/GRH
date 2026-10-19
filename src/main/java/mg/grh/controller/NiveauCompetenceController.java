package mg.grh.controller;

import mg.grh.model.NiveauCompetence;
import mg.grh.service.NiveauCompetenceService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/niveau-competence")
public class NiveauCompetenceController extends GenericController<NiveauCompetence> {
    public NiveauCompetenceController(NiveauCompetenceService service) {
        super(service);
    }
}
