package mg.grh.controller.web;

import mg.grh.service.OffreEmploiService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** Après la validation du DG, le RH publie les offres d'emploi des postes du besoin. */
@Controller
@RequestMapping("/admin/offres")
public class AdminOffreWebController {
    private final OffreEmploiService offreEmploiService;

    public AdminOffreWebController(OffreEmploiService offreEmploiService) { this.offreEmploiService = offreEmploiService; }

    @GetMapping
    public String liste(Model model) {
        model.addAttribute("offres", offreEmploiService.offresAPublier());
        return "admin/offres";
    }

    @PostMapping("/{id}/publier")
    public String publier(@PathVariable Integer id, RedirectAttributes ra) {
        try {
            var offre = offreEmploiService.publier(id);
            ra.addFlashAttribute("succes", "L'offre « " + offre.getTitre() + " » est publiée sur le site carrières.");
        } catch (RuntimeException e) {
            ra.addFlashAttribute("erreur", e.getMessage());
        }
        return "redirect:/admin/offres";
    }
}
