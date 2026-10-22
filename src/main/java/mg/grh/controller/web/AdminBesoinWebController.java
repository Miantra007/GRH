package mg.grh.controller.web;

import jakarta.servlet.http.HttpSession;
import mg.grh.dto.BesoinDetailsForm;
import mg.grh.service.BesoinService;
import mg.grh.web.SessionUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/besoins")
public class AdminBesoinWebController {
    private final BesoinService besoinService;

    public AdminBesoinWebController(BesoinService besoinService) { this.besoinService = besoinService; }

    @GetMapping
    public String liste(Model model) {
        model.addAttribute("besoins", besoinService.enAttenteRh());
        return "admin/besoins";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        model.addAttribute("besoin", besoinService.findById(id));
        model.addAttribute("postes", besoinService.postesARetraiterParRh(id));
        model.addAttribute("formulaire", besoinService.formulaireRh(id));
        return "admin/besoin-details";
    }

    @PostMapping("/{id}")
    public String traiter(@PathVariable Integer id, @ModelAttribute BesoinDetailsForm formulaire,
            HttpSession session, RedirectAttributes ra) {
        try {
            besoinService.traiterParRh(id, rh(session), formulaire);
            ra.addFlashAttribute("succes", "Les formulaires et salaires ont été transmis à la Finance.");
            return "redirect:/admin/besoins";
        } catch (RuntimeException e) {
            ra.addFlashAttribute("erreur", e.getMessage());
            return "redirect:/admin/besoins/" + id;
        }
    }

    private Integer rh(HttpSession session) {
        Object utilisateur = session.getAttribute("user");
        if (utilisateur instanceof SessionUser user && "RH".equals(user.getType())) return user.getId();
        throw new SecurityException("Accès réservé aux RH.");
    }
}
