package mg.grh.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Table(name = "contrat_candidat")
@DynamicInsert
@Getter
@Setter
@NoArgsConstructor
public class ContratCandidat implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_contrat_candidat")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_candidat", nullable = false)
    private Candidat candidat;

    @Column(name = "type_contrat", length = 3)
    private String typeContrat;

    @ManyToOne
    @JoinColumn(name = "id_contrat_ce")
    private ContratCe contratCe;

    @ManyToOne
    @JoinColumn(name = "id_contrat_cdd")
    private ContratCdd contratCdd;

    @ManyToOne
    @JoinColumn(name = "id_contrat_cdi")
    private ContratCdi contratCdi;

    /** Candidature (donc offre, poste et besoin) qui a abouti à ce contrat ; null pour un contrat sans besoin. */
    @Column(name = "id_candidature")
    private Integer idCandidature;

    /** Statut courant = statut du contrat (CE, CDD ou CDI) sous-jacent. */
    @Transient
    public StatutContrat getStatutActuel() {
        if ("CE".equals(typeContrat) && contratCe != null) return contratCe.getStatut();
        if ("CDD".equals(typeContrat) && contratCdd != null) return contratCdd.getStatut();
        if ("CDI".equals(typeContrat) && contratCdi != null) return contratCdi.getStatut();
        return null;
    }
}
