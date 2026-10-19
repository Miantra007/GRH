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
@Table(name = "contrat_ce")
@Getter
@Setter
public class ContratCe implements Entite {

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

    @ManyToOne
    @JoinColumn(name = "langue_id")
    private LangueContrat langue;

    @ManyToOne
    @JoinColumn(name = "contrat_initial_id")
    private ContratCe contratInitial;

    @Column(name = "poste", length = 100)
    private String poste;

    @ManyToOne
    @JoinColumn(name = "categorie_id")
    private CategorieProfessionnelle categorie;

    @Column(name = "indice_classification", length = 100)
    private String indiceClassification;

    @ManyToOne
    @JoinColumn(name = "convention_id")
    private ConventionCollective convention;

    @Column(name = "missions", columnDefinition = "text")
    private String missions;

    @Column(name = "lieu_travail", length = 100)
    private String lieuTravail;

    @Column(name = "duree_mois")
    private Integer dureeMois;

    @Column(name = "date_debut")
    private LocalDate dateDebut;

    @Column(name = "heures_hebdo", precision = 4, scale = 1)
    private BigDecimal heuresHebdo;

    @Column(name = "remuneration_nette", precision = 12, scale = 0)
    private BigDecimal remunerationNette;

    @Column(name = "lieu_signature", length = 100)
    private String lieuSignature;

    @Column(name = "date_signature")
    private LocalDate dateSignature;

    @Column(name = "nb_exemplaires")
    private Integer nbExemplaires = 2;

    @Override
    public String getAffichage() {
        return "CE n°" + id + (travailleur != null ? " — " + travailleur.getNomPrenoms() : "");
    }
}
