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

import java.time.LocalDate;

@Entity
@Table(name = "travailleur")
@Getter
@Setter
public class Travailleur implements Entite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "civilite_id")
    private Civilite civilite;

    @Column(name = "nom_prenoms", length = 100)
    private String nomPrenoms;

    @Column(name = "date_naissance")
    private LocalDate dateNaissance;

    @Column(name = "lieu_naissance", length = 100)
    private String lieuNaissance;

    @Column(name = "fils_de", length = 100)
    private String filsDe;

    @ManyToOne
    @JoinColumn(name = "nationalite_id")
    private Nationalite nationalite;

    @ManyToOne
    @JoinColumn(name = "situation_famille_id")
    private SituationFamille situationFamille;

    @Column(name = "nombre_enfants")
    private Integer nombreEnfants;

    @Column(name = "adresse_residence", length = 100)
    private String adresseResidence;

    @Column(name = "profession", length = 100)
    private String profession;

    @Column(name = "telephone", length = 100)
    private String telephone;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "cin_numero", length = 100)
    private String cinNumero;

    @Column(name = "cin_date_delivrance")
    private LocalDate cinDateDelivrance;

    @Column(name = "cin_lieu_delivrance", length = 100)
    private String cinLieuDelivrance;

    @Column(name = "numero_cnaps", length = 100)
    private String numeroCnaps;

    @Override
    public String getAffichage() {
        return nomPrenoms;
    }
}
