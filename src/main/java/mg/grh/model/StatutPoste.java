package mg.grh.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Table(name = "statut_poste")
@DynamicInsert
@Getter
@Setter
@NoArgsConstructor
public class StatutPoste implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_statut_poste")
    private Integer id;

    @Column(name = "libelle", length = 20)
    private String libelle;
}
