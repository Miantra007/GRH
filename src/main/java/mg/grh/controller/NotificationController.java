package mg.grh.controller;

// NotificationController.java

import mg.grh.model.Notification;
import mg.grh.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpSession;
import mg.grh.web.SessionUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationRepository notifications;

    @GetMapping("/candidats/{id}/notifications")
    public String pourCandidat(@PathVariable Integer id, HttpSession session, Model model) {
        SessionUser user = (SessionUser) session.getAttribute("user");
        if (user == null || !"CANDIDAT".equals(user.getType()) || !user.getId().equals(id)) return "redirect:/login?refuse";
        List<Notification> liste = notifications.findByIdCandidatOrderByDateCreationDesc(id);
        marquerLues(liste);
        model.addAttribute("liste", liste);
        model.addAttribute("candidat", true);
        model.addAttribute("proprietaire", id);
        return "proposition/notifications";
    }

    @GetMapping("/rh/{id}/notifications")
    public String pourRh(@PathVariable Integer id, HttpSession session, Model model) {
        SessionUser user = (SessionUser) session.getAttribute("user");
        if (user == null || !"RH".equals(user.getType()) || !user.getId().equals(id)) return "redirect:/login?refuse";
        List<Notification> liste = notifications.findByIdResponsableOrderByDateCreationDesc(id);
        marquerLues(liste);
        model.addAttribute("liste", liste);
        model.addAttribute("candidat", false);
        model.addAttribute("proprietaire", id);
        return "proposition/notifications";
    }

    /** Marque comme lues les notifications affichées (vide le badge du menu). */
    private void marquerLues(List<Notification> liste) {
        List<Notification> aMettreAJour = liste.stream().filter(n -> !Boolean.TRUE.equals(n.getLu())).toList();
        if (!aMettreAJour.isEmpty()) {
            aMettreAJour.forEach(n -> n.setLu(true));
            notifications.saveAll(aMettreAJour);
        }
    }
}
