package mg.grh.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "statut_offre")
@Getter @Setter @NoArgsConstructor
public class StatutOffre implements Identifiable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_statut_offre")
    private Integer id;
    @Column(name = "libelle", nullable = false, unique = true, length = 30)
    private String libelle;
}
