package mg.grh.controller;

import mg.grh.model.Education;
import mg.grh.service.EducationService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/education")
public class EducationController extends GenericController<Education> {
    public EducationController(EducationService service) {
        super(service);
    }
}
