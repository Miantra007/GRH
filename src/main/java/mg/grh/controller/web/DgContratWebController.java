package mg.grh.controller.web;

import mg.grh.model.ContratCandidat;
import mg.grh.service.ContratCandidatService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/** Côté Direction générale : valide les contrats signés (statut "Validé"). */
@Controller
@RequestMapping("/dg")
public class DgContratWebController {

    private final ContratCandidatService contratService;

    public DgContratWebController(ContratCandidatService contratService) {
        this.contratService = contratService;
    }

    @GetMapping("/contrats")
    public String liste(@RequestParam(defaultValue = "false") boolean tous, Model model) {
        List<ContratCandidat> contrats = tous ? contratService.findAll() : contratService.findAValider();
        model.addAttribute("contrats", contrats);
        model.addAttribute("tous", tous);
        return "dg/contrats";
    }

    @GetMapping("/contrats/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        model.addAttribute("cc", contratService.findById(id));
        model.addAttribute("historique", contratService.historique(id));
        return "contrat-detail";
    }

    @PostMapping("/contrats/{id}/valider")
    public String valider(@PathVariable Integer id, RedirectAttributes ra) {
        try {
            contratService.valider(id);
            ra.addFlashAttribute("succes", "Contrat validé.");
        } catch (RuntimeException e) {
            ra.addFlashAttribute("erreur", e.getMessage());
        }
        return "redirect:/dg/contrats/" + id;
    }
}
