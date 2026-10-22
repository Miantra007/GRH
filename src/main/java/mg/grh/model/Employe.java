package mg.grh.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import java.time.LocalDate;

@Entity
@Table(name = "employe")
@DynamicInsert
@Getter
@Setter
@NoArgsConstructor
public class Employe implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_employe")
    private Integer id;

    @Column(name = "nom_employe", length = 50)
    private String nomEmploye;

    @ManyToOne
    @JoinColumn(name = "id_poste_fille")
    private PosteFille posteFille;

    @Column(name = "date_embauche")
    private LocalDate dateEmbauche;

    @ManyToOne
    @JoinColumn(name = "id_statut_employe", nullable = false)
    private StatutEmploye statut;
}
