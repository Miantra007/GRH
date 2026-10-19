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
@Table(name = "test_technique")
@Getter
@Setter
public class TestTechnique implements Entite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_test_technique")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_candidat")
    private Candidat candidat;

    @Column(name = "description", length = 50)
    private String description;

    @Column(name = "date_test")
    private LocalDate dateTest;

    @Override
    public String getAffichage() {
        return "Test" + (candidat != null ? " — " + candidat.getAffichage() : "");
    }
}
