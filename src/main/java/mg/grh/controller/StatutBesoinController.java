package mg.grh.controller;

import mg.grh.model.StatutBesoin;
import mg.grh.service.StatutBesoinService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/statut-besoin")
public class StatutBesoinController extends GenericController<StatutBesoin> {
    public StatutBesoinController(StatutBesoinService service) {
        super(service);
    }
}
