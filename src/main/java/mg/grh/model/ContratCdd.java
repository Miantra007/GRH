package mg.grh.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "contrat_cdd")
@DynamicInsert
@Getter
@Setter
@NoArgsConstructor
public class ContratCdd implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "reference", length = 100, unique = true)
    private String reference;

    @ManyToOne
    @JoinColumn(name = "employeur_id", nullable = false)
    private Employeur employeur;

    @ManyToOne
    @JoinColumn(name = "travailleur_id", nullable = false)
    private Travailleur travailleur;

    @ManyToOne
    @JoinColumn(name = "poste_id", nullable = false)
    private Poste poste;

    @ManyToOne
    @JoinColumn(name = "categorie_id")
    private CategorieProfessionnelle categorie;

    @ManyToOne
    @JoinColumn(name = "convention_id")
    private ConventionCollective convention;

    @ManyToOne
    @JoinColumn(name = "motif_id", nullable = false)
    private MotifCdd motif;

    @ManyToOne
    @JoinColumn(name = "statut_id", nullable = false)
    private StatutContrat statut;

    @Column(name = "motif_precision")
    private String motifPrecision;

    @Column(name = "regime_juridique", length = 100)
    private String regimeJuridique;

    @Column(name = "date_debut", nullable = false)
    private LocalDate dateDebut;

    @Column(name = "date_fin", nullable = false)
    private LocalDate dateFin;

    @Column(name = "essai_duree")
    private Integer essaiDuree;

    @Column(name = "essai_date_fin")
    private LocalDate essaiDateFin;

    @Column(name = "essai_resultat", length = 100)
    private String essaiResultat;

    @Column(name = "duree_hebdo_heures")
    private BigDecimal dureeHebdoHeures;

    @Column(name = "horaire_travail", length = 100)
    private String horaireTravail;

    @Column(name = "jours_travail_semaine")
    private Integer joursTravailSemaine;

    @Column(name = "lieu_travail", length = 100)
    private String lieuTravail;

    @Column(name = "salaire_base", nullable = false)
    private BigDecimal salaireBase;

    @Column(name = "devise", length = 3)
    private String devise;

    @Column(name = "periodicite_paiement", length = 100)
    private String periodicitePaiement;

    @Column(name = "indice_classification", length = 100)
    private String indiceClassification;

    @Column(name = "conge_jours_par_mois")
    private BigDecimal congeJoursParMois;

    @Column(name = "date_conclusion")
    private LocalDate dateConclusion;

    @Column(name = "lieu_conclusion", length = 100)
    private String lieuConclusion;

    @Column(name = "nombre_exemplaires")
    private Integer nombreExemplaires;

    @Column(name = "date_visa_inspection")
    private LocalDate dateVisaInspection;

    @Column(name = "tribunal_competent", length = 100)
    private String tribunalCompetent;
}
