package mg.grh.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "test_technique")
@DynamicInsert
@Getter
@Setter
@NoArgsConstructor
public class TestTechnique implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_test_technique")
    private Integer id;

    @Column(name = "description", length = 50)
    private String description;

    @ManyToOne
    @JoinColumn(name = "id_candidature", nullable = false)
    private Candidature candidature;

    @Column(name = "date_test")
    private LocalDate dateTest;

    @Column(name = "heure_test")
    private LocalTime heureTest;

    @Column(name = "lieu")
    private String lieu;

    @Column(name = "consignes")
    private String consignes;

    @Column(name = "message_candidat")
    private String messageCandidat;
}
