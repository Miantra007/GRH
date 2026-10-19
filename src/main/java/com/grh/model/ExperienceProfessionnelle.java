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
@Table(name = "experience_professionnelle")
@Getter
@Setter
public class ExperienceProfessionnelle implements Entite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_experience")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_cv")
    private Cv cv;

    @Column(name = "poste", length = 150)
    private String poste;

    @Column(name = "entreprise", length = 150)
    private String entreprise;

    @Column(name = "type_contrat", length = 50)
    private String typeContrat;

    @Column(name = "lieu", length = 150)
    private String lieu;

    @Column(name = "date_debut")
    private LocalDate dateDebut;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Override
    public String getAffichage() {
        return poste + (entreprise != null ? " — " + entreprise : "");
    }
}
