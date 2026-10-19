package mg.grh.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import java.time.LocalDate;

@Entity
@Table(name = "texte_legal")
@DynamicInsert
@Getter
@Setter
@NoArgsConstructor
public class TexteLegal implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "reference", length = 100, nullable = false)
    private String reference;

    @Column(name = "date_texte")
    private LocalDate dateTexte;

    @Column(name = "intitule", length = 100, nullable = false)
    private String intitule;
}
