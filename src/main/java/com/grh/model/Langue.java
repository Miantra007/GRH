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
@Table(name = "langue")
@Getter
@Setter
public class Langue implements Entite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_langue")
    private Integer id;

    @Column(name = "nom_langue", length = 100)
    private String nomLangue;

    @Override
    public String getAffichage() {
        return nomLangue;
    }
}
