package mg.grh.controller;

import mg.grh.model.PosteFille;
import mg.grh.service.PosteFilleService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/poste-fille")
public class PosteFilleController extends GenericController<PosteFille> {
    public PosteFilleController(PosteFilleService service) {
        super(service);
    }
}
