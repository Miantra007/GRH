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
@Table(name = "formation")
@Getter
@Setter
public class Formation implements Entite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_formation")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_cv")
    private Cv cv;

    @Column(name = "nom_formation", length = 100)
    private String nomFormation;

    @Column(name = "etablissement", length = 200)
    private String etablissement;

    @Column(name = "date_debut")
    private LocalDate dateDebut;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Override
    public String getAffichage() {
        return nomFormation;
    }
}
