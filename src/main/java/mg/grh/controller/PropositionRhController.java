package mg.grh.controller;
// PropositionRhController.java

import mg.grh.service.PropositionService;
import mg.grh.exception.RegleMetierException;
import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpSession;
import mg.grh.web.SessionUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/rh/propositions/{idProposition}")
public class PropositionRhController {

    private final PropositionService service;

    @GetMapping
    public String voir(@PathVariable Integer idProposition, HttpSession session, Model model) {
        if (!estRh(session) || !service.estRhTraitant(idProposition, ((SessionUser) session.getAttribute("user")).getId())) return "redirect:/login?refuse";
        model.addAttribute("vue", service.vueRh(idProposition));
        return "proposition/rh-decision";
    }

    @PostMapping("/decision")
    public String decider(@PathVariable Integer idProposition,
            HttpSession session,
            @RequestParam String decision,
            @RequestParam(required = false) String commentaire,
            RedirectAttributes ra) {
        if (!estRh(session) || !service.estRhTraitant(idProposition, ((SessionUser) session.getAttribute("user")).getId())) return "redirect:/login?refuse";
        try {
            service.decider(idProposition, "VALIDER".equals(decision), commentaire);
            ra.addFlashAttribute("succes", "Décision enregistrée, le candidat a été notifié.");
        } catch (RegleMetierException e) {
            ra.addFlashAttribute("erreur", e.getMessage());
        }
        return "redirect:/rh/propositions/" + idProposition;
    }

    private boolean estRh(HttpSession session) {
        SessionUser user = (SessionUser) session.getAttribute("user");
        return user != null && "RH".equals(user.getType());
    }
}
