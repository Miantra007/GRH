package mg.grh.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "avenant_cdd")
@DynamicInsert
@Getter
@Setter
@NoArgsConstructor
public class AvenantCdd implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "contrat_id", nullable = false)
    private ContratCdd contrat;

    @ManyToOne
    @JoinColumn(name = "type_avenant_id", nullable = false)
    private TypeAvenant typeAvenant;

    @Column(name = "numero", nullable = false)
    private Integer numero;

    @Column(name = "date_avenant", nullable = false)
    private LocalDate dateAvenant;

    @Column(name = "nouvelle_date_fin")
    private LocalDate nouvelleDateFin;

    @Column(name = "nouveau_salaire")
    private BigDecimal nouveauSalaire;

    @Column(name = "objet")
    private String objet;
}
