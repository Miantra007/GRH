package mg.grh.controller;

import mg.grh.model.TypeBesoin;
import mg.grh.service.TypeBesoinService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/type-besoin")
public class TypeBesoinController extends GenericController<TypeBesoin> {
    public TypeBesoinController(TypeBesoinService service) {
        super(service);
    }
}
