package mg.grh.controller;

import mg.grh.model.NiveauLangue;
import mg.grh.service.NiveauLangueService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/niveau-langue")
public class NiveauLangueController extends GenericController<NiveauLangue> {
    public NiveauLangueController(NiveauLangueService service) {
        super(service);
    }
}
