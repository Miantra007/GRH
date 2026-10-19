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
@Table(name = "education")
@Getter
@Setter
public class Education implements Entite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_education")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_cv")
    private Cv cv;

    @Column(name = "diplome", length = 150)
    private String diplome;

    @Column(name = "etablissement", length = 150)
    private String etablissement;

    @Column(name = "domaine_etude", length = 150)
    private String domaineEtude;

    @Column(name = "niveau", length = 50)
    private String niveau;

    @Column(name = "date_debut")
    private LocalDate dateDebut;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Override
    public String getAffichage() {
        return diplome;
    }
}
