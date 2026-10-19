package mg.grh.controller;

import mg.grh.model.TypeAvenant;
import mg.grh.service.TypeAvenantService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/type-avenant")
public class TypeAvenantController extends GenericController<TypeAvenant> {
    public TypeAvenantController(TypeAvenantService service) {
        super(service);
    }
}
