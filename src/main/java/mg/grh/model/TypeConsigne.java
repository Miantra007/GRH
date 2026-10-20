package mg.grh.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Table(name = "type_consigne")
@DynamicInsert
@Getter
@Setter
@NoArgsConstructor
public class TypeConsigne implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_type_consigne")
    private Integer id;

    /** FORMATION | COMPTE_BANCAIRE | MATERIEL | DOCUMENT | AUTRE */
    @Column(name = "code", length = 30, nullable = false, unique = true)
    private String code;

    @Column(name = "libelle", length = 100, nullable = false)
    private String libelle;
}
