package mg.grh.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "reponse_critere")
@Getter
@Setter
public class ReponseCritere implements Identifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reponse_critere")
    private Integer id;
    private String libelle;
}