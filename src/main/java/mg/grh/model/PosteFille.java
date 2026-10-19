package mg.grh.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Table(name = "poste_fille")
@DynamicInsert
@Getter
@Setter
@NoArgsConstructor
public class PosteFille implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_poste_fille")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_poste", nullable = false)
    private Poste poste;

    @ManyToOne
    @JoinColumn(name = "id_dept", nullable = false)
    private Departement dept;

    @ManyToOne
    @JoinColumn(name = "id_statut_poste")
    private StatutPoste statutPoste;
}
