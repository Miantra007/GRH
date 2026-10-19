package mg.grh.controller;

import mg.grh.model.HistoriqueStatutContrat;
import mg.grh.service.HistoriqueStatutContratService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/historique-statut-contrat")
public class HistoriqueStatutContratController extends GenericController<HistoriqueStatutContrat> {
    public HistoriqueStatutContratController(HistoriqueStatutContratService service) {
        super(service);
    }
}
