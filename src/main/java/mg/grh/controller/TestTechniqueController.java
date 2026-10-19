package mg.grh.controller;

import mg.grh.model.TestTechnique;
import mg.grh.service.TestTechniqueService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test-technique")
public class TestTechniqueController extends GenericController<TestTechnique> {
    public TestTechniqueController(TestTechniqueService service) {
        super(service);
    }
}
