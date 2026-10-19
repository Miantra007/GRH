package mg.grh.controller;

import mg.grh.model.ContratCdd;
import mg.grh.service.ContratCddService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/contrat-cdd")
public class ContratCddController extends GenericController<ContratCdd> {
    public ContratCddController(ContratCddService service) {
        super(service);
    }
}
