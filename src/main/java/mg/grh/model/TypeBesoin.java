package mg.grh.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Table(name = "type_besoin")
@DynamicInsert
@Getter
@Setter
@NoArgsConstructor
public class TypeBesoin implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_type_besoin")
    private Integer id;

    @Column(name = "libelle", length = 30, nullable = false, unique = true)
    private String libelle;
}
