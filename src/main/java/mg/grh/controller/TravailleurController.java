package mg.grh.controller;

import mg.grh.model.Travailleur;
import mg.grh.service.TravailleurService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/travailleur")
public class TravailleurController extends GenericController<Travailleur> {
    public TravailleurController(TravailleurService service) {
        super(service);
    }
}
