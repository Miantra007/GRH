package mg.grh.controller;

import mg.grh.model.Poste;
import mg.grh.service.PosteService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/poste")
public class PosteController extends GenericController<Poste> {
    public PosteController(PosteService service) {
        super(service);
    }
}
