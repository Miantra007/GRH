package mg.grh.controller;

import mg.grh.model.HistoriqueEmploye;
import mg.grh.service.HistoriqueEmployeService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/historique-employe")
public class HistoriqueEmployeController extends GenericController<HistoriqueEmploye> {
    public HistoriqueEmployeController(HistoriqueEmployeService service) {
        super(service);
    }
}
