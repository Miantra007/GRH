package mg.grh.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import java.time.LocalDateTime;

@Entity
@Table(name = "cv")
@DynamicInsert
@Getter
@Setter
@NoArgsConstructor
public class Cv implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cv")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_candidat", nullable = false)
    private Candidat candidat;

    @Column(name = "titre_cv", length = 150)
    private String titreCv;

    @Column(name = "profil_professionnel")
    private String profilProfessionnel;

    @Column(name = "adresse", length = 255)
    private String adresse;

    @ManyToOne
    @JoinColumn(name = "id_ville")
    private Ville ville;

    @Column(name = "photo_url")
    private String photoUrl;

    @Column(name = "linkedin_url")
    private String linkedinUrl;

    @Column(name = "github_url")
    private String githubUrl;

    @Column(name = "date_creation")
    private LocalDateTime dateCreation;
}
