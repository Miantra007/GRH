package mg.grh.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import java.time.LocalDate;

@Entity
@Table(name = "experience_professionnelle")
@DynamicInsert
@Getter
@Setter
@NoArgsConstructor
public class ExperienceProfessionnelle implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_experience")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_cv", nullable = false)
    private Cv cv;

    @Column(name = "poste", length = 150, nullable = false)
    private String poste;

    @Column(name = "entreprise", length = 150)
    private String entreprise;

    @Column(name = "type_contrat", length = 50)
    private String typeContrat;

    @Column(name = "lieu", length = 150)
    private String lieu;

    @Column(name = "date_debut")
    private LocalDate dateDebut;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    @Column(name = "description")
    private String description;
}
