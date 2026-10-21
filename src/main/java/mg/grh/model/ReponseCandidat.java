package mg.grh.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity @Table(name="reponse_candidat") @Getter @Setter @NoArgsConstructor
public class ReponseCandidat implements Identifiable {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="id_reponse_candidat") private Integer id;
    @Column(name="libelle", nullable=false, unique=true, length=10) private String libelle;
}
