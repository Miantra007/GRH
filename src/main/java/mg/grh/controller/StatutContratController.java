package mg.grh.controller;

import mg.grh.model.StatutContrat;
import mg.grh.service.StatutContratService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/statut-contrat")
public class StatutContratController extends GenericController<StatutContrat> {
    public StatutContratController(StatutContratService service) {
        super(service);
    }
}
