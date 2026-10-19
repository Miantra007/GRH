package mg.grh.controller;

import mg.grh.model.Nationalite;
import mg.grh.service.NationaliteService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/nationalite")
public class NationaliteController extends GenericController<Nationalite> {
    public NationaliteController(NationaliteService service) {
        super(service);
    }
}
