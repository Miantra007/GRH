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

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "avenant_cdd")
@Getter
@Setter
public class AvenantCdd implements Entite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "contrat_id")
    private ContratCdd contrat;

    @ManyToOne
    @JoinColumn(name = "type_avenant_id")
    private TypeAvenant typeAvenant;

    @Column(name = "numero")
    private Integer numero;

    @Column(name = "date_avenant")
    private LocalDate dateAvenant;

    @Column(name = "nouvelle_date_fin")
    private LocalDate nouvelleDateFin;

    @Column(name = "nouveau_salaire", precision = 12, scale = 0)
    private BigDecimal nouveauSalaire;

    @Column(name = "objet", columnDefinition = "text")
    private String objet;

    @Override
    public String getAffichage() {
        return "Avenant n°" + numero + (contrat != null ? " — " + contrat.getAffichage() : "");
    }
}
