package mg.grh.controller.web;

import mg.grh.model.ContratCandidat;
import mg.grh.model.Notification;
import mg.grh.model.PropositionEmbauche;
import mg.grh.repository.ContratCandidatRepository;
import mg.grh.repository.NotificationRepository;
import mg.grh.repository.PropositionEmbaucheRepository;
import mg.grh.web.SessionUser;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CandidatNotificationWebController {
    private final NotificationRepository notifications;
    private final PropositionEmbaucheRepository propositions;
    private final ContratCandidatRepository contrats;

    public CandidatNotificationWebController(NotificationRepository notifications,
            PropositionEmbaucheRepository propositions, ContratCandidatRepository contrats) {
        this.notifications = notifications;
        this.propositions = propositions;
        this.contrats = contrats;
    }

    @GetMapping("/candidat/notifications")
    public String notifications(HttpSession session, Model model) {
        SessionUser user = (SessionUser) session.getAttribute("user");
        List<Notification> liste = notifications.findByIdCandidatOrderByDateCreationDesc(user.getId());
        // Marquer comme lues pour vider le badge du menu
        List<Notification> nonLues = liste.stream().filter(n -> !Boolean.TRUE.equals(n.getLu())).toList();
        if (!nonLues.isEmpty()) {
            nonLues.forEach(n -> n.setLu(true));
            notifications.saveAll(nonLues);
        }
        model.addAttribute("notifications", liste);
        // Statut de chaque proposition citée par une notification : permet d'afficher
        // le bon bouton (remplir le formulaire d'acceptation ou choisir le contrat).
        Map<Integer, PropositionEmbauche> propositionsParId = new HashMap<>();
        for (Notification n : liste) {
            if (n.getIdProposition() != null && !propositionsParId.containsKey(n.getIdProposition())) {
                propositions.findById(n.getIdProposition())
                        .ifPresent(p -> propositionsParId.put(n.getIdProposition(), p));
            }
        }
        model.addAttribute("propositions", propositionsParId);
        Map<Integer, String> statutContratParProposition = new HashMap<>();
        for (ContratCandidat c : contrats.findByCandidatId(user.getId())) {
            if (c.getProposition() != null && c.getStatutActuel() != null)
                statutContratParProposition.put(c.getProposition().getId(), c.getStatutActuel().getLibelle());
        }
        model.addAttribute("statutContrats", statutContratParProposition);
        return "candidat/notifications";
    }
}
