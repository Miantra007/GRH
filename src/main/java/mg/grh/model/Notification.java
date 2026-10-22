package mg.grh.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "notification")
@Getter @Setter
public class Notification implements Identifiable{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_notification")
    private Integer id;

    @Column(name = "id_candidat")
    private Integer idCandidat;

    @Column(name = "id_responsable")
    private Integer idResponsable;

    @Column(name = "id_proposition")
    private Integer idProposition;

    private String titre;
    private String message;

    @Column(name = "date_creation")
    private LocalDateTime dateCreation = LocalDateTime.now();

    private Boolean lu = false;

    /** Exactement un destinataire : candidat OU responsable (contrainte CHECK de la table). */
    public static Notification pour(Integer idCandidat, Integer idResponsable,
                                    Integer idProposition, String titre, String message) {
        Notification n = new Notification();
        n.idCandidat = idCandidat;
        n.idResponsable = idResponsable;
        n.idProposition = idProposition;
        n.titre = titre;
        n.message = message;
        return n;
    }
}