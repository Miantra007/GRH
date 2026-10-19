package mg.grh.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Table(name = "direction")
@DynamicInsert
@Getter
@Setter
@NoArgsConstructor
public class Direction implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_direction")
    private Integer id;

    @Column(name = "nom_direction", length = 100)
    private String nomDirection;

    @ManyToOne
    @JoinColumn(name = "id_directeur")
    private Responsable directeur;
}
