package mg.grh.controller;

import mg.grh.model.Employe;
import mg.grh.service.EmployeService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/employe")
public class EmployeController extends GenericController<Employe> {
    public EmployeController(EmployeService service) {
        super(service);
    }
}
