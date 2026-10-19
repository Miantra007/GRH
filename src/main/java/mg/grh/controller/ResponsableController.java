package mg.grh.controller;

import mg.grh.model.Responsable;
import mg.grh.service.ResponsableService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/responsable")
public class ResponsableController extends GenericController<Responsable> {
    public ResponsableController(ResponsableService service) {
        super(service);
    }
}
