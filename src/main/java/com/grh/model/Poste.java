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
@Table(name = "poste")
@Getter
@Setter
public class Poste implements Entite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "intitule", length = 100)
    private String intitule;

    @ManyToOne
    @JoinColumn(name = "categorie_id")
    private CategorieProfessionnelle categorie;

    @ManyToOne
    @JoinColumn(name = "id_responsable")
    private Responsable responsable;

    @Column(name = "date_creation")
    private LocalDate dateCreation;

    @Override
    public String getAffichage() {
        return intitule;
    }
}
