package mg.grh.controller;

import mg.grh.model.StatutPoste;
import mg.grh.service.StatutPosteService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/statut-poste")
public class StatutPosteController extends GenericController<StatutPoste> {
    public StatutPosteController(StatutPosteService service) {
        super(service);
    }
}
