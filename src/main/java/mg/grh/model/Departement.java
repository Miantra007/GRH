package mg.grh.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Table(name = "departement")
@DynamicInsert
@Getter
@Setter
@NoArgsConstructor
public class Departement implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_dept")
    private Integer id;

    @Column(name = "nom_dept", length = 50)
    private String nomDept;

    @ManyToOne
    @JoinColumn(name = "id_responsable")
    private Responsable responsable;
}
