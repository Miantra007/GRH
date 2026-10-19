package mg.grh.controller.web;

import jakarta.servlet.http.HttpSession;
import mg.grh.model.ContratCandidat;
import mg.grh.service.ContratCandidatService;
import mg.grh.web.SessionUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** Côté Candidat : consulte ses contrats et les signe (statut "Signé"). */
@Controller
@RequestMapping("/candidat")
public class CandidatContratWebController {

    private final ContratCandidatService contratService;

    public CandidatContratWebController(ContratCandidatService contratService) {
        this.contratService = contratService;
    }

    @GetMapping("/contrats")
    public String mesContrats(HttpSession session, Model model) {
        model.addAttribute("contrats", contratService.findByCandidat(user(session).getId()));
        return "candidat/contrats";
    }

    @GetMapping("/contrats/{id}")
    public String detail(@PathVariable Integer id, HttpSession session, Model model, RedirectAttributes ra) {
        ContratCandidat cc = contratService.findById(id);
        if (!cc.getCandidat().getId().equals(user(session).getId())) {
            ra.addFlashAttribute("erreur", "Ce contrat ne vous appartient pas.");
            return "redirect:/candidat/contrats";
        }
        model.addAttribute("cc", cc);
        model.addAttribute("historique", contratService.historique(id));
        return "contrat-detail";
    }

    @PostMapping("/contrats/{id}/signer")
    public String signer(@PathVariable Integer id, HttpSession session, RedirectAttributes ra) {
        try {
            contratService.signer(id, user(session).getId());
            ra.addFlashAttribute("succes", "Contrat signé. Il est maintenant en attente de validation par la Direction générale.");
        } catch (RuntimeException e) {
            ra.addFlashAttribute("erreur", e.getMessage());
        }
        return "redirect:/candidat/contrats/" + id;
    }

    private SessionUser user(HttpSession session) {
        return (SessionUser) session.getAttribute("user");
    }
}
