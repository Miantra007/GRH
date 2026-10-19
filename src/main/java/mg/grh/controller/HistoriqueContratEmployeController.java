package mg.grh.controller;

import mg.grh.model.HistoriqueContratEmploye;
import mg.grh.service.HistoriqueContratEmployeService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/historique-contrat-employe")
public class HistoriqueContratEmployeController extends GenericController<HistoriqueContratEmploye> {
    public HistoriqueContratEmployeController(HistoriqueContratEmployeService service) {
        super(service);
    }
}
