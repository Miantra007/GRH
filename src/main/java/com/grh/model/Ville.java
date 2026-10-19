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

@Entity
@Table(name = "ville")
@Getter
@Setter
public class Ville implements Entite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ville")
    private Integer id;

    @Column(name = "nom_ville", length = 100)
    private String nomVille;

    @ManyToOne
    @JoinColumn(name = "id_pays")
    private Pays pays;

    @Override
    public String getAffichage() {
        return nomVille;
    }
}
