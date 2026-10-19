package mg.grh.controller;

import mg.grh.model.CvLangue;
import mg.grh.service.CvLangueService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cv-langue")
public class CvLangueController extends GenericController<CvLangue> {
    public CvLangueController(CvLangueService service) {
        super(service);
    }
}
