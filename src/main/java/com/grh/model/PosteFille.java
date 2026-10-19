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
@Table(name = "poste_fille")
@Getter
@Setter
public class PosteFille implements Entite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_poste_fille")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_poste")
    private Poste poste;

    @ManyToOne
    @JoinColumn(name = "id_dept")
    private Departement departement;

    @ManyToOne
    @JoinColumn(name = "id_statut_poste")
    private StatutPoste statutPoste;

    @Override
    public String getAffichage() {
        return (poste != null ? poste.getIntitule() : "?") + " — " + (departement != null ? departement.getNomDept() : "?");
    }
}
