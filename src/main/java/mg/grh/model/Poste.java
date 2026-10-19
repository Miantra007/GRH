package mg.grh.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import java.time.LocalDate;

@Entity
@Table(name = "poste")
@DynamicInsert
@Getter
@Setter
@NoArgsConstructor
public class Poste implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_poste")
    private Integer id;

    @Column(name = "libelle", length = 50)
    private String libelle;

    @ManyToOne
    @JoinColumn(name = "id_direction")
    private Direction direction;

    @Column(name = "date_creation")
    private LocalDate dateCreation;
}
