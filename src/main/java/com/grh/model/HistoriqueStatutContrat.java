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
@Table(name = "historique_statut_contrat")
@Getter
@Setter
public class HistoriqueStatutContrat implements Entite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_historique_statut_contrat")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_contrat_candidat")
    private ContratCandidat contratCandidat;

    @ManyToOne
    @JoinColumn(name = "id_statut_contrat")
    private StatutContrat statutContrat;

    @Column(name = "date")
    private LocalDate date;

    @Override
    public String getAffichage() {
        return (statutContrat != null ? statutContrat.getLibelle() : "?") + (date != null ? " — " + date : "");
    }
}
