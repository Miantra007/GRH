package mg.grh.controller;

import mg.grh.model.CategorieProfessionnelle;
import mg.grh.service.CategorieProfessionnelleService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categorie-professionnelle")
public class CategorieProfessionnelleController extends GenericController<CategorieProfessionnelle> {
    public CategorieProfessionnelleController(CategorieProfessionnelleService service) {
        super(service);
    }
}
