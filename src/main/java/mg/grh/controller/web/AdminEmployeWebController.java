package mg.grh.controller.web;

import mg.grh.service.EmployeService;
import mg.grh.service.HistoriqueEmployeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/** Côté RH : liste des employés et historique (employés remplacés, suspendus). */
@Controller
@RequestMapping("/admin")
public class AdminEmployeWebController {

    private final EmployeService employeService;
    private final HistoriqueEmployeService historiqueService;

    public AdminEmployeWebController(EmployeService employeService, HistoriqueEmployeService historiqueService) {
        this.employeService = employeService;
        this.historiqueService = historiqueService;
    }

    @GetMapping("/employes")
    public String employes(@RequestParam(defaultValue = "false") boolean tous, Model model) {
        model.addAttribute("employes", tous ? employeService.tous() : employeService.enPoste());
        model.addAttribute("tous", tous);
        return "admin/employes";
    }

    @GetMapping("/employes/historique")
    public String historique(Model model) {
        model.addAttribute("historique", historiqueService.recents());
        return "admin/historique-employes";
    }
}
