package mg.grh.controller;

import mg.grh.model.Entretien;
import mg.grh.service.EntretienService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/entretien")
public class EntretienController extends GenericController<Entretien> {
    public EntretienController(EntretienService service) {
        super(service);
    }
}
