package mg.grh.controller;

import mg.grh.model.ContratCdi;
import mg.grh.service.ContratCdiService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/contrat-cdi")
public class ContratCdiController extends GenericController<ContratCdi> {
    public ContratCdiController(ContratCdiService service) {
        super(service);
    }
}
