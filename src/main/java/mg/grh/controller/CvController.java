package mg.grh.controller;

import mg.grh.model.Cv;
import mg.grh.service.CvService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cv")
public class CvController extends GenericController<Cv> {
    public CvController(CvService service) {
        super(service);
    }
}
