package mg.grh.controller;

import mg.grh.model.RuptureCdd;
import mg.grh.service.RuptureCddService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rupture-cdd")
public class RuptureCddController extends GenericController<RuptureCdd> {
    public RuptureCddController(RuptureCddService service) {
        super(service);
    }
}
