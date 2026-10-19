package mg.grh.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Table(name = "pays")
@DynamicInsert
@Getter
@Setter
@NoArgsConstructor
public class Pays implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pays")
    private Integer id;

    @Column(name = "nom_pays", length = 100, nullable = false, unique = true)
    private String nomPays;
}
