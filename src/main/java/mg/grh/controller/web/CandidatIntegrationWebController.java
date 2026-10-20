package mg.grh.controller.web;

import jakarta.servlet.http.HttpSession;
import mg.grh.model.ConsigneIntegration;
import mg.grh.service.ConsigneIntegrationService;
import mg.grh.web.SessionUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/** Côté Candidat : consulte ses consignes d'intégration et les marque comme faites. */
@Controller
@RequestMapping("/candidat/integration")
public class CandidatIntegrationWebController {

    private final ConsigneIntegrationService consigneService;

    public CandidatIntegrationWebController(ConsigneIntegrationService consigneService) {
        this.consigneService = consigneService;
    }

    @GetMapping
    public String mesConsignes(HttpSession session, Model model) {
        List<ConsigneIntegration> consignes = consigneService.findByCandidat(user(session).getId());
        model.addAttribute("aFaire", consignes.stream().filter(c -> !c.isFaite()).toList());
        model.addAttribute("faites", consignes.stream().filter(ConsigneIntegration::isFaite).toList());
        model.addAttribute("total", consignes.size());
        return "candidat/integration";
    }

    @PostMapping("/{id}/faite")
    public String faite(@PathVariable Integer id, @RequestParam(required = false) String commentaire,
                        HttpSession session, RedirectAttributes ra) {
        try {
            consigneService.marquerFaite(id, user(session).getId(), commentaire);
            ra.addFlashAttribute("succes", "Consigne marquée comme faite.");
        } catch (RuntimeException e) {
            ra.addFlashAttribute("erreur", e.getMessage());
        }
        return "redirect:/candidat/integration";
    }

    @PostMapping("/{id}/rouvrir")
    public String rouvrir(@PathVariable Integer id, HttpSession session, RedirectAttributes ra) {
        try {
            consigneService.rouvrir(id, user(session).getId());
            ra.addFlashAttribute("succes", "Consigne remise à faire.");
        } catch (RuntimeException e) {
            ra.addFlashAttribute("erreur", e.getMessage());
        }
        return "redirect:/candidat/integration";
    }

    private SessionUser user(HttpSession session) {
        return (SessionUser) session.getAttribute("user");
    }
}
