package mg.grh.controller;

import mg.grh.model.ContratCe;
import mg.grh.service.ContratCeService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/contrat-ce")
public class ContratCeController extends GenericController<ContratCe> {
    public ContratCeController(ContratCeService service) {
        super(service);
    }
}
