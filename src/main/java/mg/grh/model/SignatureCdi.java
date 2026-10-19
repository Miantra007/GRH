package mg.grh.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Table(name = "signature_cdi")
@DynamicInsert
@Getter
@Setter
@NoArgsConstructor
public class SignatureCdi implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "contrat_id", nullable = false)
    private ContratCdi contrat;

    @Column(name = "role", length = 100, nullable = false)
    private String role;

    @Column(name = "signe")
    private Boolean signe;
}
