package mg.grh.controller;

import mg.grh.model.ExperienceProfessionnelle;
import mg.grh.service.ExperienceProfessionnelleService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/experience-professionnelle")
public class ExperienceProfessionnelleController extends GenericController<ExperienceProfessionnelle> {
    public ExperienceProfessionnelleController(ExperienceProfessionnelleService service) {
        super(service);
    }
}
