package mg.grh.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "offre_emploi")
@Getter @Setter @NoArgsConstructor
public class OffreEmploi implements Identifiable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_offre")
    private Integer id;
    @Column(name = "id_besoin_poste", nullable = false)
    private Integer besoinPosteId;
    @Column(name = "titre", nullable = false, length = 150)
    private String titre;
    @Column(name = "description")
    private String description;
    @Column(name = "profil_recherche")
    private String profilRecherche;
    @Column(name = "date_publication")
    private LocalDateTime datePublication;
    @Column(name = "date_limite")
    private LocalDate dateLimite;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_statut_offre", nullable = false)
    private StatutOffre statutOffre;
    @Column(name = "visible_client", nullable = false)
    private boolean visibleClient;
}
