package mg.grh.controller;

import mg.grh.model.Ville;
import mg.grh.service.VilleService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ville")
public class VilleController extends GenericController<Ville> {
    public VilleController(VilleService service) {
        super(service);
    }
}
