package mg.grh.controller;

import mg.grh.model.TypeContrat;
import mg.grh.service.TypeContratService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/type-contrat")
public class TypeContratController extends GenericController<TypeContrat> {
    public TypeContratController(TypeContratService service) {
        super(service);
    }
}
