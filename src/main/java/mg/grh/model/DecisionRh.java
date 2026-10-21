package mg.grh.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity @Table(name="decision_rh") @Getter @Setter @NoArgsConstructor
public class DecisionRh implements Identifiable {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="id_decision_rh") private Integer id;
    @Column(name="libelle", nullable=false, unique=true, length=10) private String libelle;
}
