package mg.grh.controller;

import mg.grh.model.TypeConsigne;
import mg.grh.service.TypeConsigneService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/type-consigne")
public class TypeConsigneController extends GenericController<TypeConsigne> {
    public TypeConsigneController(TypeConsigneService service) {
        super(service);
    }
}
