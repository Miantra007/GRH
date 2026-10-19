package mg.grh.controller;

import mg.grh.model.TexteLegal;
import mg.grh.service.TexteLegalService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/texte-legal")
public class TexteLegalController extends GenericController<TexteLegal> {
    public TexteLegalController(TexteLegalService service) {
        super(service);
    }
}
