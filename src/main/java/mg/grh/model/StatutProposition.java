package mg.grh.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "statut_proposition")
@Getter 
@Setter
@NoArgsConstructor
public class StatutProposition implements Identifiable{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_statut_proposition")
    private Integer id;
    private String libelle;
}