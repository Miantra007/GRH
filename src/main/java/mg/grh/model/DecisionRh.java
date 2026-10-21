package mg.grh.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "decision_rh")
@Getter @Setter
public class DecisionRh  implements  Identifiable{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_decision_rh")
    private Integer id;
    private String libelle;
}
