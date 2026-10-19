package mg.grh.controller;

import mg.grh.model.Formation;
import mg.grh.service.FormationService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/formation")
public class FormationController extends GenericController<Formation> {
    public FormationController(FormationService service) {
        super(service);
    }
}
