package mg.grh.controller;

import mg.grh.model.TypeRupture;
import mg.grh.service.TypeRuptureService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/type-rupture")
public class TypeRuptureController extends GenericController<TypeRupture> {
    public TypeRuptureController(TypeRuptureService service) {
        super(service);
    }
}
