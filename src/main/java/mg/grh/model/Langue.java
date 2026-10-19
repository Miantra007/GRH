package mg.grh.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Table(name = "langue")
@DynamicInsert
@Getter
@Setter
@NoArgsConstructor
public class Langue implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_langue")
    private Integer id;

    @Column(name = "nom_langue", length = 100, nullable = false, unique = true)
    private String nomLangue;
}
