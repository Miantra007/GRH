package mg.grh.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "reponse_candidat")
@Getter
@Setter
public class ReponseCandidat implements Identifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reponse_candidat")
    private Integer id;
    private String libelle;
}