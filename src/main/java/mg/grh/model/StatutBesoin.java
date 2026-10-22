package mg.grh.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Table(name = "statut_besoin")
@DynamicInsert
@Getter
@Setter
@NoArgsConstructor
public class StatutBesoin implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_statut_besoin")
    private Integer id;

    @Column(name = "libelle", length = 30, nullable = false, unique = true)
    private String libelle;
}
