package mg.grh.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDate;
import java.time.LocalDateTime;

/** Période pendant laquelle un employé a eu un statut donné (ex : suspendu parce qu'il a été remplacé). */
@Entity
@Table(name = "historique_employe")
@DynamicInsert
@Getter
@Setter
@NoArgsConstructor
public class HistoriqueEmploye implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_historique_employe")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_employe", nullable = false)
    private Employe employe;

    @ManyToOne
    @JoinColumn(name = "id_statut_employe", nullable = false)
    private StatutEmploye statut;

    @Column(name = "date_debut", nullable = false)
    private LocalDate dateDebut;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    @Column(name = "motif")
    private String motif;

    @Column(name = "date_enregistrement")
    private LocalDateTime dateEnregistrement;
}
