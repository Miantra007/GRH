package mg.grh.controller;

import jakarta.servlet.http.HttpSession;
import mg.grh.dto.BesoinCreationForm;
import mg.grh.model.Besoin;
import mg.grh.service.BesoinService;
import mg.grh.web.SessionUser;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/besoin")
public class BesoinController extends GenericController<Besoin> {
    private final BesoinService besoinService;

    public BesoinController(BesoinService service) {
        super(service);
        this.besoinService = service;
    }

    /** API de création groupée, réservée au directeur connecté. */
    @PostMapping("/directeur")
    @ResponseStatus(HttpStatus.CREATED)
    public Besoin creerPourDirecteur(@RequestBody BesoinCreationForm formulaire, HttpSession session) {
        Object utilisateur = session.getAttribute("user");
        if (!(utilisateur instanceof SessionUser user) || !"DIRECTEUR".equals(user.getType())) {
            throw new SecurityException("Seul un directeur connecté peut créer un besoin.");
        }
        return besoinService.creerPourDirecteur(user.getId(), formulaire);
    }
}
