package mg.grh.controller;

import mg.grh.model.Besoin;
import mg.grh.service.BesoinService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/besoin")
public class BesoinController extends GenericController<Besoin> {
    public BesoinController(BesoinService service) {
        super(service);
    }
}
