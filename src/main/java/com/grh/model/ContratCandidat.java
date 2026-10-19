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
@Table(name = "contrat_candidat")
@Getter
@Setter
public class ContratCandidat implements Entite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_contrat_candidat")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_candidat")
    private Candidat candidat;

    @ManyToOne
    @JoinColumn(name = "id_contrat_ce")
    private ContratCe contratCe;

    @ManyToOne
    @JoinColumn(name = "id_contrat_cdd")
    private ContratCdd contratCdd;

    @ManyToOne
    @JoinColumn(name = "id_contrat_cdi")
    private ContratCdi contratCdi;

    @Override
    public String getAffichage() {
        return (candidat != null ? candidat.getAffichage() : "?") + " — " + (contratCdi != null ? contratCdi.getAffichage() : contratCdd != null ? contratCdd.getAffichage() : contratCe != null ? contratCe.getAffichage() : "?");
    }
}
