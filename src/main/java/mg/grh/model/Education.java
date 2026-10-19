package mg.grh.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import java.time.LocalDate;

@Entity
@Table(name = "education")
@DynamicInsert
@Getter
@Setter
@NoArgsConstructor
public class Education implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_education")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_cv", nullable = false)
    private Cv cv;

    @Column(name = "diplome", length = 150, nullable = false)
    private String diplome;

    @Column(name = "etablissement", length = 150)
    private String etablissement;

    @Column(name = "domaine_etude", length = 150)
    private String domaineEtude;

    @Column(name = "niveau", length = 50)
    private String niveau;

    @Column(name = "date_debut")
    private LocalDate dateDebut;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    @Column(name = "description")
    private String description;
}
