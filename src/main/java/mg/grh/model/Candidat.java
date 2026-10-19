package mg.grh.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;

@Entity
@Table(name = "candidat")
@DynamicInsert
@Getter
@Setter
@NoArgsConstructor
public class Candidat implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_candidat")
    private Integer id;

    @Column(name = "nom_candidat", length = 50)
    private String nomCandidat;

    @Column(name = "prenom_candidat", length = 50)
    private String prenomCandidat;

    @Column(name = "email", length = 150, nullable = false, unique = true)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "mot_de_passe", length = 255, nullable = false)
    private String motDePasse;

    @Column(name = "dtn")
    private LocalDate dtn;
}
