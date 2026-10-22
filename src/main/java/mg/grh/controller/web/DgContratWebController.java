package mg.grh.controller.web;

import mg.grh.model.ContratCandidat;
import mg.grh.service.EmbaucheService;
import mg.grh.service.ContratCandidatService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/** Consultation DG des contrats et du besoin associé. */
@Controller
@RequestMapping("/dg")
public class DgContratWebController {

    private final ContratCandidatService contratService;
    private final EmbaucheService embaucheService;

    public DgContratWebController(ContratCandidatService contratService, EmbaucheService embaucheService) {
        this.contratService = contratService;
        this.embaucheService = embaucheService;
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
        ContratCandidat cc = contratService.findById(id);
        model.addAttribute("cc", cc);
        model.addAttribute("historique", contratService.historique(id));
        model.addAttribute("besoin", embaucheService.apercu(cc));
        model.addAttribute("embauche", embaucheService.embauchePour(id).orElse(null));
        return "contrat-detail";
    }

}
