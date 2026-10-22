package mg.grh.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

/** ACTIF | EN_CONGE | SUSPENDU | DEMISSIONNAIRE | LICENCIE | RETRAITE */
@Entity
@Table(name = "statut_employe")
@DynamicInsert
@Getter
@Setter
@NoArgsConstructor
public class StatutEmploye implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_statut_employe")
    private Integer id;

    @Column(name = "libelle", length = 20, nullable = false, unique = true)
    private String libelle;
}
