package mg.grh.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Table(name = "type_contrat")
@DynamicInsert
@Getter
@Setter
@NoArgsConstructor
public class TypeContrat implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_type_contrat")
    private Integer id;

    @Column(name = "libelle", length = 10, nullable = false, unique = true)
    private String libelle;
}
