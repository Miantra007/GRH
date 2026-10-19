package mg.grh.controller;

import mg.grh.model.AvenantCdd;
import mg.grh.service.AvenantCddService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/avenant-cdd")
public class AvenantCddController extends GenericController<AvenantCdd> {
    public AvenantCddController(AvenantCddService service) {
        super(service);
    }
}
