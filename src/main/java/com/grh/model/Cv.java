package com.grh.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "cv")
@Getter
@Setter
public class Cv implements Entite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cv")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_candidat")
    private Candidat candidat;

    @Column(name = "titre_cv", length = 150)
    private String titreCv;

    @Column(name = "profil_professionnel", columnDefinition = "text")
    private String profilProfessionnel;

    @Column(name = "adresse", length = 255)
    private String adresse;

    @ManyToOne
    @JoinColumn(name = "id_ville")
    private Ville ville;

    @Column(name = "photo_url", columnDefinition = "text")
    private String photoUrl;

    @Column(name = "linkedin_url", columnDefinition = "text")
    private String linkedinUrl;

    @Column(name = "github_url", columnDefinition = "text")
    private String githubUrl;

    @CreationTimestamp
    @Column(name = "date_creation", updatable = false)
    private LocalDateTime dateCreation;

    @Override
    public String getAffichage() {
        return (titreCv != null ? titreCv : "CV n°" + id) + (candidat != null ? " — " + candidat.getAffichage() : "");
    }
}
