package mg.grh.controller;

import mg.grh.model.ElementSalaireCdd;
import mg.grh.service.ElementSalaireCddService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/element-salaire-cdd")
public class ElementSalaireCddController extends GenericController<ElementSalaireCdd> {
    public ElementSalaireCddController(ElementSalaireCddService service) {
        super(service);
    }
}
