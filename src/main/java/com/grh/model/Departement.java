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
@Table(name = "departement")
@Getter
@Setter
public class Departement implements Entite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_dept")
    private Integer id;

    @Column(name = "nom_dept", length = 50)
    private String nomDept;

    @ManyToOne
    @JoinColumn(name = "id_responsable")
    private Responsable responsable;

    @Override
    public String getAffichage() {
        return nomDept;
    }
}
