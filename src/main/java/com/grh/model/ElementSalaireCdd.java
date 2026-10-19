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
@Table(name = "element_salaire_cdd")
@Getter
@Setter
public class ElementSalaireCdd implements Entite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "contrat_id")
    private ContratCdd contrat;

    @Column(name = "libelle", length = 100)
    private String libelle;

    @Column(name = "montant", precision = 12, scale = 0)
    private BigDecimal montant;

    @Column(name = "nature", length = 100)
    private String nature;

    @Override
    public String getAffichage() {
        return libelle;
    }
}
