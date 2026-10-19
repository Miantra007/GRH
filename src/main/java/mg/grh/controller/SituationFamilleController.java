package mg.grh.controller;

import mg.grh.model.SituationFamille;
import mg.grh.service.SituationFamilleService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/situation-famille")
public class SituationFamilleController extends GenericController<SituationFamille> {
    public SituationFamilleController(SituationFamilleService service) {
        super(service);
    }
}
