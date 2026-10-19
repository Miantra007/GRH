package com.grh.model;

import com.grh.model.enums.RoleSignataire;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "signature_cdi")
@Getter
@Setter
public class SignatureCdi implements Entite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "contrat_id")
    private ContratCdi contrat;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", length = 100)
    private RoleSignataire role;

    @Column(name = "signe")
    private Boolean signe = Boolean.FALSE;

    @Override
    public String getAffichage() {
        return (role != null ? role.getLibelle() : "?") + (contrat != null ? " — " + contrat.getAffichage() : "");
    }
}
