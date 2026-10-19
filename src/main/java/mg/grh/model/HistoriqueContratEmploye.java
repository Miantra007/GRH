package mg.grh.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import java.time.LocalDate;

@Entity
@Table(name = "historique_contrat_employe")
@DynamicInsert
@Getter
@Setter
@NoArgsConstructor
public class HistoriqueContratEmploye implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_historique_contrat_employe")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_employe")
    private Employe employe;

    @ManyToOne
    @JoinColumn(name = "id_contrat_candidat")
    private ContratCandidat contratCandidat;

    @Column(name = "date_embauche")
    private LocalDate dateEmbauche;
}
