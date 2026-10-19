package mg.grh.controller;

import mg.grh.model.CentreInteret;
import mg.grh.service.CentreInteretService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/centre-interet")
public class CentreInteretController extends GenericController<CentreInteret> {
    public CentreInteretController(CentreInteretService service) {
        super(service);
    }
}
