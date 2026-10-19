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
@Table(name = "responsable")
@Getter
@Setter
public class Responsable implements Entite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_responsable")
    private Integer id;

    @Column(name = "email", length = 150)
    private String email;

    @Column(name = "mot_de_passe", length = 255)
    private String motDePasse;

    @ManyToOne
    @JoinColumn(name = "id_role")
    private Role role;

    @Column(name = "date_embauche")
    private LocalDate dateEmbauche;

    @Override
    public String getAffichage() {
        return email;
    }
}
