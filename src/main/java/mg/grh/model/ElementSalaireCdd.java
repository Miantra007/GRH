package mg.grh.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import java.math.BigDecimal;

@Entity
@Table(name = "element_salaire_cdd")
@DynamicInsert
@Getter
@Setter
@NoArgsConstructor
public class ElementSalaireCdd implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "contrat_id", nullable = false)
    private ContratCdd contrat;

    @Column(name = "libelle", length = 100, nullable = false)
    private String libelle;

    @Column(name = "montant", nullable = false)
    private BigDecimal montant;

    @Column(name = "nature", length = 100)
    private String nature;
}
