package mg.grh.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "notification")
@Getter @Setter @NoArgsConstructor
public class Notification implements Identifiable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_notification")
    private Integer id;
    @ManyToOne @JoinColumn(name = "id_candidat")
    private Candidat candidat;
    @ManyToOne @JoinColumn(name = "id_responsable")
    private Responsable responsable;
    @ManyToOne @JoinColumn(name = "id_proposition")
    private PropositionEmbauche proposition;
    @ManyToOne @JoinColumn(name = "id_candidature")
    private Candidature candidature;
    @Column(name = "titre", nullable = false, length = 150)
    private String titre;
    @Column(name = "message", nullable = false)
    private String message;
    @Column(name = "date_creation", nullable = false)
    private LocalDateTime dateCreation;
    @Column(name = "lu", nullable = false)
    private boolean lu;
}
