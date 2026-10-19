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
@Table(name = "cv_langue")
@Getter
@Setter
public class CvLangue implements Entite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cv_langue")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_cv")
    private Cv cv;

    @ManyToOne
    @JoinColumn(name = "id_langue")
    private Langue langue;

    @ManyToOne
    @JoinColumn(name = "id_niveau_langue")
    private NiveauLangue niveauLangue;

    @Override
    public String getAffichage() {
        return (langue != null ? langue.getNomLangue() : "?") + (niveauLangue != null ? " — " + niveauLangue.getLibelleNiveau() : "");
    }
}
