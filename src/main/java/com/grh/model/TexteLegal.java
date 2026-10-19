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
@Table(name = "texte_legal")
@Getter
@Setter
public class TexteLegal implements Entite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "reference", length = 100)
    private String reference;

    @Column(name = "date_texte")
    private LocalDate dateTexte;

    @Column(name = "intitule", length = 100)
    private String intitule;

    @Override
    public String getAffichage() {
        return reference + " " + intitule;
    }
}
