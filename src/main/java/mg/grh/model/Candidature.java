package mg.grh.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "candidature", uniqueConstraints = @UniqueConstraint(name = "uk_candidature_offre_candidat", columnNames = {"id_offre", "id_candidat"}))
@Getter @Setter @NoArgsConstructor
public class Candidature implements Identifiable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_candidature")
    private Integer id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_offre", nullable = false)
    private OffreEmploi offre;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_candidat", nullable = false)
    private Candidat candidat;
    @Column(name = "date_candidature", nullable = false)
    private LocalDateTime dateCandidature;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_statut_candidature", nullable = false)
    private StatutCandidature statut;
}
