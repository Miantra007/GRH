package com.grh.model;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "niveau_competence")
@Getter
@Setter
public class NiveauCompetence implements Entite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_niveau_competence")
    private Integer id;

    @Column(name = "libelle_niveau", length = 50)
    private String libelleNiveau;

    @Override
    public String getAffichage() {
        return libelleNiveau;
    }
}
