package mg.grh.controller;

// PropositionCandidatController.java

import mg.grh.dto.ReponseForm;
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
@RequestMapping("/candidats/{idCandidat}/propositions/{idProposition}")
public class PropositionCandidatController {

    private final PropositionService service;

    @GetMapping
    public String formulaire(@PathVariable Integer idCandidat,
            @PathVariable Integer idProposition, HttpSession session, Model model) {
        if (!estProprietaire(session, idCandidat)) return "redirect:/login?refuse";
        model.addAttribute("vue", service.vueCandidat(idCandidat, idProposition));
        return "proposition/formulaire";
    }

    @PostMapping
    public String soumettre(@PathVariable Integer idCandidat,
            @PathVariable Integer idProposition,
            HttpSession session,
            @ModelAttribute ReponseForm form,
            RedirectAttributes ra) {
        if (!estProprietaire(session, idCandidat)) return "redirect:/login?refuse";
        try {
            service.soumettre(idCandidat, idProposition, form);
            ra.addFlashAttribute("succes", "Votre réponse a bien été enregistrée.");
        } catch (RegleMetierException e) {
            ra.addFlashAttribute("erreur", e.getMessage());
        }
        return "redirect:/candidats/" + idCandidat + "/propositions/" + idProposition;
    }

    private boolean estProprietaire(HttpSession session, Integer id) {
        SessionUser user = (SessionUser) session.getAttribute("user");
        return user != null && "CANDIDAT".equals(user.getType()) && user.getId().equals(id);
    }
}
