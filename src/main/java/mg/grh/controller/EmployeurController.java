package mg.grh.controller;

import mg.grh.model.Employeur;
import mg.grh.service.EmployeurService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/employeur")
public class EmployeurController extends GenericController<Employeur> {
    public EmployeurController(EmployeurService service) {
        super(service);
    }
}
