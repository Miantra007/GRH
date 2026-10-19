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
@Table(name = "competence")
@Getter
@Setter
public class Competence implements Entite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_competence")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_cv")
    private Cv cv;

    @Column(name = "nom_competence", length = 100)
    private String nomCompetence;

    @ManyToOne
    @JoinColumn(name = "id_niveau_competence")
    private NiveauCompetence niveauCompetence;

    @Override
    public String getAffichage() {
        return nomCompetence;
    }
}
