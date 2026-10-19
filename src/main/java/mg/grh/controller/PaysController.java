package mg.grh.controller;

import mg.grh.model.Pays;
import mg.grh.service.PaysService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pays")
public class PaysController extends GenericController<Pays> {
    public PaysController(PaysService service) {
        super(service);
    }
}
