package mg.grh.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "besoin_poste", uniqueConstraints = @UniqueConstraint(columnNames = { "id_besoin", "id_poste" }))
@DynamicInsert
@Getter
@Setter
@NoArgsConstructor
public class BesoinPoste implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_besoin_poste")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_besoin", nullable = false)
    private Besoin besoin;

    @ManyToOne
    @JoinColumn(name = "id_poste", nullable = false)
    private Poste poste;

    @Column(name = "nombre_postes", nullable = false)
    private Integer nombrePostes;

    @Column(name = "missions")
    private String missions;

    @ManyToOne
    @JoinColumn(name = "id_type_contrat")
    private TypeContrat typeContrat;

    @Column(name = "salaire_propose_rh", precision = 12, scale = 0)
    private BigDecimal salaireProposeRh;

    @Column(name = "salaire_valide_finance", precision = 12, scale = 0)
    private BigDecimal salaireValideFinance;

    @Column(name = "horaire_travail", length = 150)
    private String horaireTravail;

    @Column(name = "responsable_hierarchique", length = 150)
    private String responsableHierarchique;

    @Column(name = "date_prise_poste")
    private LocalDate datePrisePoste;
}
