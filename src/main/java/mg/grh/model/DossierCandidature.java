package mg.grh.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "dossier_candidature")
@Getter @Setter @NoArgsConstructor
public class DossierCandidature implements Identifiable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_dossier")
    private Integer id;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_candidature", nullable = false, unique = true)
    private Candidature candidature;
    @Column(name = "lettre_motivation", nullable = false, columnDefinition = "TEXT")
    private String lettreMotivation;
    @Column(name = "cv_importe", length = 255)
    private String cvImporte;
    @Column(name = "date_creation", nullable = false)
    private LocalDateTime dateCreation;
}
