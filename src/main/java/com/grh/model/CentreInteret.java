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
@Table(name = "centre_interet")
@Getter
@Setter
public class CentreInteret implements Entite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_centre_interet")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_cv")
    private Cv cv;

    @Column(name = "nom_centre_interet", length = 150)
    private String nomCentreInteret;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Override
    public String getAffichage() {
        return nomCentreInteret;
    }
}
