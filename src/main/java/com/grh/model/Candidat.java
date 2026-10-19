package com.grh.model;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "candidat")
@Getter
@Setter
public class Candidat implements Entite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_candidat")
    private Integer id;

    @Column(name = "nom_candidat", length = 50)
    private String nomCandidat;

    @Column(name = "prenom_candidat", length = 50)
    private String prenomCandidat;

    @Column(name = "email", length = 150)
    private String email;

    @Column(name = "mot_de_passe", length = 255)
    private String motDePasse;

    @Column(name = "dtn")
    private LocalDate dtn;

    @Override
    public String getAffichage() {
        return (prenomCandidat != null ? prenomCandidat + " " : "") + (nomCandidat != null ? nomCandidat : email);
    }
}
