package mg.grh.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import mg.grh.repository.NotificationRepository;
import mg.grh.service.OffreEmploiService;
import mg.grh.web.SessionUser;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/** Attributs disponibles dans toutes les pages : lien actif du menu + badge des notifications non lues. */
@ControllerAdvice
public class ModeleGlobal {

    private final NotificationRepository notifications;
    private final OffreEmploiService offreEmploiService;

    public ModeleGlobal(NotificationRepository notifications, OffreEmploiService offreEmploiService) {
        this.notifications = notifications;
        this.offreEmploiService = offreEmploiService;
    }

    /** Badge « Offres à publier » du menu RH : postes validés par le DG dont l'offre n'est pas encore publiée. */
    @ModelAttribute("offresAPublier")
    public long offresAPublier(HttpSession session) {
        Object u = session == null ? null : session.getAttribute("user");
        if (!(u instanceof SessionUser user) || !"RH".equals(user.getType())) return 0;
        try {
            return offreEmploiService.nombreOffresAPublier();
        } catch (RuntimeException e) {
            return 0;
        }
    }

    @ModelAttribute("uriCourante")
    public String uriCourante(HttpServletRequest requete) {
        return requete.getRequestURI().substring(requete.getContextPath().length());
    }

    /** Nombre de notifications non lues de l'utilisateur connecté (0 si non connecté). */
    @ModelAttribute("notifsNonLues")
    public long notifsNonLues(HttpSession session) {
        Object u = session == null ? null : session.getAttribute("user");
        if (!(u instanceof SessionUser user)) return 0;
        try {
            if ("CANDIDAT".equals(user.getType())) return notifications.countByIdCandidatAndLuFalse(user.getId());
            if ("RH".equals(user.getType())) return notifications.countByIdResponsableAndLuFalse(user.getId());
        } catch (RuntimeException e) {
            return 0;
        }
        return 0;
    }
}
