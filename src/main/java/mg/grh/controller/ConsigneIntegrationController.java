package mg.grh.controller;

import mg.grh.model.ConsigneIntegration;
import mg.grh.service.ConsigneIntegrationService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/consigne-integration")
public class ConsigneIntegrationController extends GenericController<ConsigneIntegration> {
    public ConsigneIntegrationController(ConsigneIntegrationService service) {
        super(service);
    }
}
