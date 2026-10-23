package mg.grh.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "contrat_ce")
@DynamicInsert
@Getter
@Setter
@NoArgsConstructor
public class ContratCe implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "employeur_id", nullable = false)
    private Employeur employeur;

    @ManyToOne
    @JoinColumn(name = "travailleur_id", nullable = false)
    private Travailleur travailleur;

    @ManyToOne
    @JoinColumn(name = "statut_id", nullable = false)
    private StatutContrat statut;

    @ManyToOne
    @JoinColumn(name = "langue_id")
    private LangueContrat langue;

    @ManyToOne
    @JoinColumn(name = "contrat_initial_id")
    private ContratCe contratInitial;

    @Column(name = "poste", length = 100)
    private String poste;

    @ManyToOne
    @JoinColumn(name = "categorie_id", nullable = false)
    private CategorieProfessionnelle categorie;

    @Column(name = "indice_classification", length = 100)
    private String indiceClassification;

    @ManyToOne
    @JoinColumn(name = "convention_id")
    private ConventionCollective convention;

    @Column(name = "missions")
    private String missions;

    @Column(name = "lieu_travail", length = 100)
    private String lieuTravail;

    @Column(name = "duree_mois")
    private Integer dureeMois;

    @Column(name = "date_debut")
    private LocalDate dateDebut;

    @Column(name = "heures_hebdo")
    private BigDecimal heuresHebdo;

    @Column(name = "remuneration_nette")
    private BigDecimal remunerationNette;

    @Column(name = "lieu_signature", length = 100)
    private String lieuSignature;

    @Column(name = "date_signature")
    private LocalDate dateSignature;

    @Column(name = "nb_exemplaires")
    private Integer nbExemplaires;
}
