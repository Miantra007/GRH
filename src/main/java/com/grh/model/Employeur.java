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

@Entity
@Table(name = "employeur")
@Getter
@Setter
public class Employeur implements Entite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "raison_sociale", length = 100)
    private String raisonSociale;

    @Column(name = "forme_juridique", length = 100)
    private String formeJuridique;

    @Column(name = "capital_social", precision = 15, scale = 0)
    private BigDecimal capitalSocial;

    @Column(name = "adresse_siege", length = 100)
    private String adresseSiege;

    @Column(name = "nif", length = 100)
    private String nif;

    @Column(name = "stat", length = 100)
    private String stat;

    @Column(name = "rcs", length = 100)
    private String rcs;

    @Column(name = "numero_cnaps", length = 100)
    private String numeroCnaps;

    @Column(name = "organisme_sante", length = 100)
    private String organismeSante;

    @Column(name = "numero_organisme_sante", length = 100)
    private String numeroOrganismeSante;

    @Column(name = "telephone", length = 100)
    private String telephone;

    @Column(name = "email", length = 100)
    private String email;

    @ManyToOne
    @JoinColumn(name = "civilite_id")
    private Civilite civilite;

    @Column(name = "representant_nom", length = 100)
    private String representantNom;

    @Column(name = "representant_fonction", length = 100)
    private String representantFonction;

    @Override
    public String getAffichage() {
        return raisonSociale;
    }
}
