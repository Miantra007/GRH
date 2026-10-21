package mg.grh.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity @Table(name="statut_proposition") @Getter @Setter @NoArgsConstructor
public class StatutProposition implements Identifiable {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="id_statut_proposition") private Integer id;
    @Column(name="libelle", nullable=false, unique=true, length=30) private String libelle;
}
