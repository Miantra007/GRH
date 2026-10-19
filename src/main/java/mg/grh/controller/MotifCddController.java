package mg.grh.controller;

import mg.grh.model.MotifCdd;
import mg.grh.service.MotifCddService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/motif-cdd")
public class MotifCddController extends GenericController<MotifCdd> {
    public MotifCddController(MotifCddService service) {
        super(service);
    }
}
