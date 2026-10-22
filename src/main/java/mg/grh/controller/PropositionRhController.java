package mg.grh.controller;
// PropositionRhController.java

import mg.grh.service.PropositionService;
import mg.grh.exception.RegleMetierException;
import lombok.RequiredArgsConstructor;
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
    public String voir(@PathVariable Integer idProposition, Model model) {
        model.addAttribute("vue", service.vueRh(idProposition));
        return "proposition/rh-decision";
    }

    @PostMapping("/decision")
    public String decider(@PathVariable Integer idProposition,
            @RequestParam String decision,
            @RequestParam(required = false) String commentaire,
            RedirectAttributes ra) {
        try {
            service.decider(idProposition, "VALIDER".equals(decision), commentaire);
            ra.addFlashAttribute("succes", "Décision enregistrée, le candidat a été notifié.");
        } catch (RegleMetierException e) {
            ra.addFlashAttribute("erreur", e.getMessage());
        }
        return "redirect:/rh/propositions/" + idProposition;
    }
}