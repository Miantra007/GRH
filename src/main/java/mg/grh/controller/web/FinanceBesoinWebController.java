package mg.grh.controller.web;

import jakarta.servlet.http.HttpSession;
import mg.grh.dto.DecisionFinanceForm;
import mg.grh.service.BesoinService;
import mg.grh.web.SessionUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/finance/besoins")
public class FinanceBesoinWebController {
    private final BesoinService besoinService;

    public FinanceBesoinWebController(BesoinService besoinService) { this.besoinService = besoinService; }

    @GetMapping
    public String liste(Model model) {
        model.addAttribute("besoins", besoinService.enAttenteFinance());
        return "finance/besoins";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        DecisionFinanceForm formulaire = new DecisionFinanceForm();
        besoinService.postesADeciderParFinance(id).forEach(poste -> {
            DecisionFinanceForm.Ligne ligne = new DecisionFinanceForm.Ligne();
            ligne.setBesoinPosteId(poste.getId());
            formulaire.getLignes().add(ligne);
        });
        model.addAttribute("besoin", besoinService.findById(id));
        model.addAttribute("postes", besoinService.postesADeciderParFinance(id));
        model.addAttribute("formulaire", formulaire);
        return "finance/besoin-details";
    }

    @PostMapping("/{id}")
    public String decider(@PathVariable Integer id, @ModelAttribute DecisionFinanceForm formulaire,
            HttpSession session, RedirectAttributes ra) {
        try {
            besoinService.deciderParFinance(id, finance(session), formulaire);
            ra.addFlashAttribute("succes", "Décision financière enregistrée.");
            return "redirect:/finance/besoins";
        } catch (RuntimeException e) {
            ra.addFlashAttribute("erreur", e.getMessage());
            return "redirect:/finance/besoins/" + id;
        }
    }

    private Integer finance(HttpSession session) {
        Object utilisateur = session.getAttribute("user");
        if (utilisateur instanceof SessionUser user && "FINANCE".equals(user.getType())) return user.getId();
        throw new SecurityException("Accès réservé à la Finance.");
    }
}
