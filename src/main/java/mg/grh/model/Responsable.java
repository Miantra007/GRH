package mg.grh.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;

@Entity
@Table(name = "responsable")
@DynamicInsert
@Getter
@Setter
@NoArgsConstructor
public class Responsable implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_responsable")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_role")
    private Role role;

    @Column(name = "email", length = 150, nullable = false, unique = true)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "mot_de_passe", length = 255, nullable = false)
    private String motDePasse;

    @Column(name = "date_embauche")
    private LocalDate dateEmbauche;
}
