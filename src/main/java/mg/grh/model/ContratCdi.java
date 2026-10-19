package mg.grh.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "contrat_cdi")
@DynamicInsert
@Getter
@Setter
@NoArgsConstructor
public class ContratCdi implements Identifiable {

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

    @Column(name = "date_prise_fonction", nullable = false)
    private LocalDate datePriseFonction;

    @Column(name = "emploi", length = 100, nullable = false)
    private String emploi;

    @Column(name = "non_lie_autre_entreprise")
    private Boolean nonLieAutreEntreprise;

    @Column(name = "etablissement", length = 100)
    private String etablissement;

    @Column(name = "region", length = 100)
    private String region;

    @Column(name = "district_commune", length = 100)
    private String districtCommune;

    @Column(name = "duree_essai", length = 100)
    private String dureeEssai;

    @Column(name = "article_reference_essai", length = 100)
    private String articleReferenceEssai;

    @Column(name = "source_essai", length = 100)
    private String sourceEssai;

    @ManyToOne
    @JoinColumn(name = "categorie_id")
    private CategorieProfessionnelle categorie;

    @ManyToOne
    @JoinColumn(name = "convention_id")
    private ConventionCollective convention;

    @Column(name = "salaire_brut_mensuel")
    private BigDecimal salaireBrutMensuel;

    @Column(name = "duree_hebdo_heures")
    private BigDecimal dureeHebdoHeures;

    @Column(name = "horaires_fixes_par", length = 100)
    private String horairesFixesPar;

    @Column(name = "lieu_recrutement", length = 100)
    private String lieuRecrutement;

    @Column(name = "regime_accident_travail", length = 100)
    private String regimeAccidentTravail;

    @Column(name = "regime_maladie_non_pro", length = 100)
    private String regimeMaladieNonPro;

    @Column(name = "entreprise_confidentialite", length = 100)
    private String entrepriseConfidentialite;

    @Column(name = "direction_regionale_travail", length = 100)
    private String directionRegionaleTravail;

    @Column(name = "nb_exemplaires")
    private Integer nbExemplaires;

    @Column(name = "lieu_signature", length = 100)
    private String lieuSignature;

    @Column(name = "date_signature")
    private LocalDate dateSignature;
}
