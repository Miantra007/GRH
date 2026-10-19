package mg.grh.controller;

import mg.grh.model.Langue;
import mg.grh.service.LangueService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/langue")
public class LangueController extends GenericController<Langue> {
    public LangueController(LangueService service) {
        super(service);
    }
}
