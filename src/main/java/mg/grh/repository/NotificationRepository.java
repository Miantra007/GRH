package mg.grh.repository;

import mg.grh.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findByIdCandidatOrderByDateCreationDesc(Integer idCandidat);

    List<Notification> findByIdResponsableOrderByDateCreationDesc(Integer idResponsable);
    

    List<Notification> findByIdCandidatureOrderByDateCreationDesc(Integer candidatureId);

    // Compteurs de notifications non lues (badge du menu)
    long countByIdCandidatAndLuFalse(Integer idCandidat);

    long countByIdResponsableAndLuFalse(Integer idResponsable);
}
