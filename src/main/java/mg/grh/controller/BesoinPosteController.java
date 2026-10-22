package mg.grh.controller;

import mg.grh.model.BesoinPoste;
import mg.grh.service.BesoinPosteService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/besoin-poste")
public class BesoinPosteController extends GenericController<BesoinPoste> {
    public BesoinPosteController(BesoinPosteService service) {
        super(service);
    }
}
