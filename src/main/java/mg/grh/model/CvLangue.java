package mg.grh.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Table(name = "cv_langue")
@DynamicInsert
@Getter
@Setter
@NoArgsConstructor
public class CvLangue implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cv_langue")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_cv", nullable = false)
    private Cv cv;

    @ManyToOne
    @JoinColumn(name = "id_langue", nullable = false)
    private Langue langue;

    @ManyToOne
    @JoinColumn(name = "id_niveau_langue", nullable = false)
    private NiveauLangue niveauLangue;
}
