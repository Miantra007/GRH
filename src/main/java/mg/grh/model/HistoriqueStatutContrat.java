package mg.grh.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import java.time.LocalDateTime;

@Entity
@Table(name = "historique_statut_contrat")
@DynamicInsert
@Getter
@Setter
@NoArgsConstructor
public class HistoriqueStatutContrat implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_historique_statut_contrat")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_statut_contrat", nullable = false)
    private StatutContrat statutContrat;

    @ManyToOne
    @JoinColumn(name = "id_contrat_candidat", nullable = false)
    private ContratCandidat contratCandidat;

    @Column(name = "date_changement")
    private LocalDateTime dateChangement;
}
