package mg.grh.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Table(name = "centre_interet")
@DynamicInsert
@Getter
@Setter
@NoArgsConstructor
public class CentreInteret implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_centre_interet")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_cv", nullable = false)
    private Cv cv;

    @Column(name = "nom_centre_interet", length = 150, nullable = false)
    private String nomCentreInteret;

    @Column(name = "description")
    private String description;
}
