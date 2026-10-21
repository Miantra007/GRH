package mg.grh.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "type_critere_proposition")
@Getter
@Setter
public class TypeCritereProposition implements Identifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_type_critere")
    private Integer id;
    private String libelle;
}