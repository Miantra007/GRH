package mg.grh.controller.web;

import jakarta.servlet.http.HttpSession;
import mg.grh.service.BesoinService;
import mg.grh.web.SessionUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/dg/besoins")
public class DgBesoinWebController {
    private final BesoinService besoinService;

    public DgBesoinWebController(BesoinService besoinService) { this.besoinService = besoinService; }

    @GetMapping
    public String liste(Model model) {
        model.addAttribute("besoins", besoinService.enAttenteDg());
        return "dg/besoins";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        model.addAttribute("besoin", besoinService.findById(id));
        model.addAttribute("postes", besoinService.postesAValiderParDg(id));
        return "dg/besoin-details";
    }

    @PostMapping("/{id}/valider")
    public String valider(@PathVariable Integer id, HttpSession session, RedirectAttributes ra) {
        try {
            besoinService.validerParDg(id, dg(session));
            ra.addFlashAttribute("succes", "Les postes affichés sont validés par la Direction générale.");
            return "redirect:/dg/besoins";
        } catch (RuntimeException e) {
            ra.addFlashAttribute("erreur", e.getMessage());
            return "redirect:/dg/besoins/" + id;
        }
    }

    private Integer dg(HttpSession session) {
        Object utilisateur = session.getAttribute("user");
        if (utilisateur instanceof SessionUser user && "DG".equals(user.getType())) return user.getId();
        throw new SecurityException("Accès réservé à la Direction générale.");
    }
}
