package mg.grh.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Table(name = "ville")
@DynamicInsert
@Getter
@Setter
@NoArgsConstructor
public class Ville implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ville")
    private Integer id;

    @Column(name = "nom_ville", length = 100, nullable = false)
    private String nomVille;

    @ManyToOne
    @JoinColumn(name = "id_pays", nullable = false)
    private Pays pays;
}
