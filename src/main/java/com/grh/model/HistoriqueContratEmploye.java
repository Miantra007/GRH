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
@Table(name = "historique_contrat_employe")
@Getter
@Setter
public class HistoriqueContratEmploye implements Entite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_historique_contrat_employe")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_employe")
    private Employe employe;

    @ManyToOne
    @JoinColumn(name = "id_contrat_candidat")
    private ContratCandidat contratCandidat;

    @Column(name = "date_embauche")
    private LocalDate dateEmbauche;

    @Override
    public String getAffichage() {
        return (employe != null ? employe.getNomEmploye() : "?") + (dateEmbauche != null ? " — " + dateEmbauche : "");
    }
}
