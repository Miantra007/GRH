package mg.grh.controller.web;

import jakarta.servlet.http.HttpSession;
import mg.grh.service.CandidatService;
import mg.grh.service.ConsigneIntegrationService;
import mg.grh.service.ContratCandidatService;
import mg.grh.service.TypeConsigneService;
import mg.grh.web.SessionUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

/** Côté RH : envoie au candidat ses consignes d'intégration (formations, compte bancaire, matériel...). */
@Controller
@RequestMapping("/admin")
public class AdminIntegrationWebController {

    private final ConsigneIntegrationService consigneService;
    private final CandidatService candidatService;
    private final ContratCandidatService contratService;
    private final TypeConsigneService typeService;

    public AdminIntegrationWebController(ConsigneIntegrationService consigneService, CandidatService candidatService,
                                         ContratCandidatService contratService, TypeConsigneService typeService) {
        this.consigneService = consigneService;
        this.candidatService = candidatService;
        this.contratService = contratService;
        this.typeService = typeService;
    }

    @GetMapping("/candidats/{id}/integration")
    public String consignes(@PathVariable Integer id, Model model) {
        model.addAttribute("candidat", candidatService.findById(id));
        model.addAttribute("consignes", consigneService.findByCandidat(id));
        model.addAttribute("contrats", contratService.findByCandidat(id));
        model.addAttribute("types", typeService.findAll());
        return "admin/integration";
    }

    @PostMapping("/candidats/{id}/integration")
    public String envoyer(@PathVariable Integer id, @RequestParam Integer typeId,
                          @RequestParam(required = false) Integer contratCandidatId,
                          @RequestParam String titre, @RequestParam(required = false) String description,
                          @RequestParam(required = false) LocalDate dateLimite,
                          HttpSession session, RedirectAttributes ra) {
        try {
            SessionUser rh = (SessionUser) session.getAttribute("user");
            consigneService.envoyer(id, rh.getId(), typeId, contratCandidatId, titre, description, dateLimite);
            ra.addFlashAttribute("succes", "Consigne envoyée au candidat.");
        } catch (RuntimeException e) {
            ra.addFlashAttribute("erreur", e.getMessage());
        }
        return "redirect:/admin/candidats/" + id + "/integration";
    }

    @PostMapping("/integration/{consigneId}/supprimer")
    public String supprimer(@PathVariable Integer consigneId, RedirectAttributes ra) {
        Integer candidatId = consigneService.findById(consigneId).getCandidat().getId();
        consigneService.delete(consigneId);
        ra.addFlashAttribute("succes", "Consigne retirée.");
        return "redirect:/admin/candidats/" + candidatId + "/integration";
    }
}
