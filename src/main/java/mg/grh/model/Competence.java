package mg.grh.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Table(name = "competence")
@DynamicInsert
@Getter
@Setter
@NoArgsConstructor
public class Competence implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_competence")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_cv", nullable = false)
    private Cv cv;

    @Column(name = "nom_competence", length = 100, nullable = false)
    private String nomCompetence;

    @ManyToOne
    @JoinColumn(name = "id_niveau_competence", nullable = false)
    private NiveauCompetence niveauCompetence;
}
