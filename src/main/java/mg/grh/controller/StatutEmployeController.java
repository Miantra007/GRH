package mg.grh.controller;

import mg.grh.model.StatutEmploye;
import mg.grh.service.StatutEmployeService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/statut-employe")
public class StatutEmployeController extends GenericController<StatutEmploye> {
    public StatutEmployeController(StatutEmployeService service) {
        super(service);
    }
}
