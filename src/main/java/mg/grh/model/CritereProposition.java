package mg.grh.model;

// CritereProposition.java

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "critere_proposition")
@Getter
@Setter
public class CritereProposition implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_critere_proposition")
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_proposition")
    private PropositionEmbauche proposition;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_type_critere")
    private TypeCritereProposition type;

    @ManyToOne
    @JoinColumn(name = "id_reponse_critere")
    private ReponseCritere reponse;

    @Column(name = "contre_proposition")
    private String contreProposition;

    @Column(name = "commentaire_candidat")
    private String commentaireCandidat;

    @Column(name = "date_reponse")
    private LocalDateTime dateReponse;
}