package mg.grh.controller;

import mg.grh.model.ConventionCollective;
import mg.grh.service.ConventionCollectiveService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/convention-collective")
public class ConventionCollectiveController extends GenericController<ConventionCollective> {
    public ConventionCollectiveController(ConventionCollectiveService service) {
        super(service);
    }
}
