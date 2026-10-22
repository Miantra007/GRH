package mg.grh.controller;

// NotificationController.java

import mg.grh.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpSession;
import mg.grh.web.SessionUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationRepository notifications;

    @GetMapping("/candidats/{id}/notifications")
    public String pourCandidat(@PathVariable Integer id, HttpSession session, Model model) {
        SessionUser user = (SessionUser) session.getAttribute("user");
        if (user == null || !"CANDIDAT".equals(user.getType()) || !user.getId().equals(id)) return "redirect:/login?refuse";
        model.addAttribute("liste", notifications.findByIdCandidatOrderByDateCreationDesc(id));
        model.addAttribute("candidat", true);
        model.addAttribute("proprietaire", id);
        return "proposition/notifications";
    }

    @GetMapping("/rh/{id}/notifications")
    public String pourRh(@PathVariable Integer id, HttpSession session, Model model) {
        SessionUser user = (SessionUser) session.getAttribute("user");
        if (user == null || !"RH".equals(user.getType()) || !user.getId().equals(id)) return "redirect:/login?refuse";
        model.addAttribute("liste", notifications.findByIdResponsableOrderByDateCreationDesc(id));
        model.addAttribute("candidat", false);
        model.addAttribute("proprietaire", id);
        return "proposition/notifications";
    }
}
