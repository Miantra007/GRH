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
@Table(name = "statut_poste")
@Getter
@Setter
public class StatutPoste implements Entite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_statut_poste")
    private Integer id;

    @Column(name = "libelle", length = 20)
    private String libelle;

    @Override
    public String getAffichage() {
        return libelle;
    }
}
