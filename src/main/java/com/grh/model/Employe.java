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
@Table(name = "employe")
@Getter
@Setter
public class Employe implements Entite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_employe")
    private Integer id;

    @Column(name = "nom_employe", length = 50)
    private String nomEmploye;

    @ManyToOne
    @JoinColumn(name = "id_poste_fille")
    private PosteFille posteFille;

    @Column(name = "date_embauche")
    private LocalDate dateEmbauche;

    @Override
    public String getAffichage() {
        return nomEmploye;
    }
}
