package mg.grh.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import java.time.LocalDate;

@Entity
@Table(name = "formation")
@DynamicInsert
@Getter
@Setter
@NoArgsConstructor
public class Formation implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_formation")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_cv", nullable = false)
    private Cv cv;

    @Column(name = "nom_formation", length = 100, nullable = false)
    private String nomFormation;

    @Column(name = "etablissement", length = 200)
    private String etablissement;

    @Column(name = "date_debut")
    private LocalDate dateDebut;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    @Column(name = "description")
    private String description;
}
