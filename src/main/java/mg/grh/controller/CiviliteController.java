package mg.grh.controller;

import mg.grh.model.Civilite;
import mg.grh.service.CiviliteService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/civilite")
public class CiviliteController extends GenericController<Civilite> {
    public CiviliteController(CiviliteService service) {
        super(service);
    }
}
