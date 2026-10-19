package com.grh.model;

import com.grh.model.enums.SourceEssai;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "contrat_cdi")
@Getter
@Setter
public class ContratCdi implements Entite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "travailleur_id")
    private Travailleur travailleur;

    @ManyToOne
    @JoinColumn(name = "employeur_id")
    private Employeur employeur;

    @ManyToOne
    @JoinColumn(name = "statut_id")
    private StatutContrat statut;

    @Column(name = "date_prise_fonction")
    private LocalDate datePriseFonction;

    @Column(name = "emploi", length = 100)
    private String emploi;

    @Column(name = "non_lie_autre_entreprise")
    private Boolean nonLieAutreEntreprise = Boolean.FALSE;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "source_essai", length = 100)
    private SourceEssai sourceEssai;

    @ManyToOne
    @JoinColumn(name = "categorie_id")
    private CategorieProfessionnelle categorie;

    @ManyToOne
    @JoinColumn(name = "convention_id")
    private ConventionCollective convention;

    @Column(name = "salaire_brut_mensuel", precision = 12, scale = 0)
    private BigDecimal salaireBrutMensuel;

    @Column(name = "duree_hebdo_heures", precision = 4, scale = 1)
    private BigDecimal dureeHebdoHeures;

    @Column(name = "horaires_fixes_par", length = 100)
    private String horairesFixesPar = "Règlement intérieur de l'entreprise";

    @Column(name = "lieu_recrutement", length = 100)
    private String lieuRecrutement;

    @Column(name = "regime_accident_travail", length = 100)
    private String regimeAccidentTravail = "Code de prévoyance sociale";

    @Column(name = "regime_maladie_non_pro", length = 100)
    private String regimeMaladieNonPro;

    @Column(name = "entreprise_confidentialite", length = 100)
    private String entrepriseConfidentialite;

    @Column(name = "direction_regionale_travail", length = 100)
    private String directionRegionaleTravail;

    @Column(name = "nb_exemplaires")
    private Integer nbExemplaires = 4;

    @Column(name = "lieu_signature", length = 100)
    private String lieuSignature;

    @Column(name = "date_signature")
    private LocalDate dateSignature;

    @Override
    public String getAffichage() {
        return "CDI n°" + id + (travailleur != null ? " — " + travailleur.getNomPrenoms() : "");
    }
}
