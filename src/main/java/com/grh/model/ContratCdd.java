package com.grh.model;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "contrat_cdd")
@Getter
@Setter
public class ContratCdd implements Entite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "reference", length = 100)
    private String reference;

    @ManyToOne
    @JoinColumn(name = "travailleur_id")
    private Travailleur travailleur;

    @ManyToOne
    @JoinColumn(name = "employeur_id")
    private Employeur employeur;

    @ManyToOne
    @JoinColumn(name = "poste_id")
    private Poste poste;

    @ManyToOne
    @JoinColumn(name = "categorie_id")
    private CategorieProfessionnelle categorie;

    @ManyToOne
    @JoinColumn(name = "convention_id")
    private ConventionCollective convention;

    @ManyToOne
    @JoinColumn(name = "motif_id")
    private MotifCdd motif;

    @ManyToOne
    @JoinColumn(name = "statut_id")
    private StatutContrat statut;

    @Column(name = "motif_precision", columnDefinition = "text")
    private String motifPrecision;

    @Column(name = "regime_juridique", length = 100)
    private String regimeJuridique = "Code du travail de Madagascar";

    @Column(name = "date_debut")
    private LocalDate dateDebut;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    @Column(name = "essai_duree")
    private Integer essaiDuree;

    @Column(name = "essai_date_fin")
    private LocalDate essaiDateFin;

    @Column(name = "essai_resultat", length = 100)
    private String essaiResultat;

    @Column(name = "duree_hebdo_heures", precision = 4, scale = 1)
    private BigDecimal dureeHebdoHeures = new BigDecimal("40");

    @Column(name = "horaire_travail", length = 100)
    private String horaireTravail;

    @Column(name = "jours_travail_semaine")
    private Integer joursTravailSemaine;

    @Column(name = "lieu_travail", length = 100)
    private String lieuTravail;

    @Column(name = "salaire_base", precision = 12, scale = 0)
    private BigDecimal salaireBase;

    @Column(name = "devise", length = 3)
    private String devise = "MGA";

    @Column(name = "periodicite_paiement", length = 100)
    private String periodicitePaiement = "Mensuelle";

    @Column(name = "indice_classification", length = 100)
    private String indiceClassification;

    @Column(name = "conge_jours_par_mois", precision = 3, scale = 1)
    private BigDecimal congeJoursParMois;

    @Column(name = "date_conclusion")
    private LocalDate dateConclusion;

    @Column(name = "lieu_conclusion", length = 100)
    private String lieuConclusion;

    @Column(name = "nombre_exemplaires")
    private Integer nombreExemplaires = 2;

    @Column(name = "date_visa_inspection")
    private LocalDate dateVisaInspection;

    @Column(name = "tribunal_competent", length = 100)
    private String tribunalCompetent = "Tribunal du travail";

    @Override
    public String getAffichage() {
        return (reference != null ? reference : "CDD n°" + id) + (travailleur != null ? " — " + travailleur.getNomPrenoms() : "");
    }
}
