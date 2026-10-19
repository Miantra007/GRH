package mg.grh.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import java.time.LocalDate;

@Entity
@Table(name = "rupture_cdd")
@DynamicInsert
@Getter
@Setter
@NoArgsConstructor
public class RuptureCdd implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "contrat_id", nullable = false)
    private ContratCdd contrat;

    @ManyToOne
    @JoinColumn(name = "type_rupture_id", nullable = false)
    private TypeRupture typeRupture;

    @Column(name = "date_rupture", nullable = false)
    private LocalDate dateRupture;

    @Column(name = "motif", length = 100)
    private String motif;

    @Column(name = "observations")
    private String observations;
}
