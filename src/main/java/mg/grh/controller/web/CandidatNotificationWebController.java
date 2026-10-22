package mg.grh.controller.web;

import mg.grh.repository.NotificationRepository;
import mg.grh.web.SessionUser;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CandidatNotificationWebController {
    private final NotificationRepository notifications;

    public CandidatNotificationWebController(NotificationRepository notifications) {
        this.notifications = notifications;
    }

    @GetMapping("/candidat/notifications")
    public String notifications(HttpSession session, Model model) {
        SessionUser user = (SessionUser) session.getAttribute("user");
        model.addAttribute("notifications", notifications.findByIdCandidatOrderByDateCreationDesc(user.getId()));
        return "candidat/notifications";
    }
}
