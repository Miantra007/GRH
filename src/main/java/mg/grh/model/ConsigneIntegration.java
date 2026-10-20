package mg.grh.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDate;
import java.time.LocalDateTime;

/** Chose que RH demande au candidat de faire avant/à son arrivée (formation, compte bancaire, matériel...). */
@Entity
@Table(name = "consigne_integration")
@DynamicInsert
@Getter
@Setter
@NoArgsConstructor
public class ConsigneIntegration implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_consigne")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_candidat", nullable = false)
    private Candidat candidat;

    /** Contrat concerné (facultatif). */
    @ManyToOne
    @JoinColumn(name = "id_contrat_candidat")
    private ContratCandidat contratCandidat;

    @ManyToOne
    @JoinColumn(name = "id_type_consigne", nullable = false)
    private TypeConsigne typeConsigne;

    @Column(name = "titre", length = 150, nullable = false)
    private String titre;

    @Column(name = "description")
    private String description;

    @Column(name = "date_limite")
    private LocalDate dateLimite;

    @ManyToOne
    @JoinColumn(name = "id_rh_emetteur")
    private Responsable rhEmetteur;

    @Column(name = "date_envoi")
    private LocalDateTime dateEnvoi;

    /** null = à faire ; renseignée = faite par le candidat. */
    @Column(name = "date_realisation")
    private LocalDateTime dateRealisation;

    @Column(name = "commentaire_candidat")
    private String commentaireCandidat;

    @Transient
    public boolean isFaite() {
        return dateRealisation != null;
    }

    /** À faire et date limite dépassée. */
    @Transient
    public boolean isEnRetard() {
        return dateRealisation == null && dateLimite != null && dateLimite.isBefore(LocalDate.now());
    }
}
