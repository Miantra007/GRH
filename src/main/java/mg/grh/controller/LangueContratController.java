package mg.grh.controller;

import mg.grh.model.LangueContrat;
import mg.grh.service.LangueContratService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/langue-contrat")
public class LangueContratController extends GenericController<LangueContrat> {
    public LangueContratController(LangueContratService service) {
        super(service);
    }
}
