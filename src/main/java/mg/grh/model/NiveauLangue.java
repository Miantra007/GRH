package mg.grh.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Table(name = "niveau_langue")
@DynamicInsert
@Getter
@Setter
@NoArgsConstructor
public class NiveauLangue implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_niveau_langue")
    private Integer id;

    @Column(name = "libelle_niveau", length = 50, nullable = false, unique = true)
    private String libelleNiveau;
}
