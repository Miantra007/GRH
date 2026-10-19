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
@Table(name = "rupture_cdd")
@Getter
@Setter
public class RuptureCdd implements Entite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "contrat_id")
    private ContratCdd contrat;

    @ManyToOne
    @JoinColumn(name = "type_rupture_id")
    private TypeRupture typeRupture;

    @Column(name = "date_rupture")
    private LocalDate dateRupture;

    @Column(name = "motif", length = 100)
    private String motif;

    @Column(name = "observations", columnDefinition = "text")
    private String observations;

    @Override
    public String getAffichage() {
        return "Rupture" + (contrat != null ? " — " + contrat.getAffichage() : "");
    }
}
