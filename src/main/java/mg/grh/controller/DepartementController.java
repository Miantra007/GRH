package mg.grh.controller;

import mg.grh.model.Departement;
import mg.grh.service.DepartementService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/departement")
public class DepartementController extends GenericController<Departement> {
    public DepartementController(DepartementService service) {
        super(service);
    }
}
