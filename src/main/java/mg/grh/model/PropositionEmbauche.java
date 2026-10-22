package mg.grh.model;

// PropositionEmbauche.java

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "proposition_embauche")
@Getter
@Setter
public class PropositionEmbauche implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_proposition")
    private Integer id;

    @Column(name = "id_candidature")
    private Integer idCandidature;

    @Column(name = "date_envoi")
    private LocalDateTime dateEnvoi;

    @ManyToOne
    @JoinColumn(name = "id_reponse_candidat")
    private ReponseCandidat reponseCandidat;

    @Column(name = "commentaire_candidat")
    private String commentaireCandidat;

    @Column(name = "contre_proposition")
    private String contreProposition;

    @Column(name = "salaire_demande")
    private BigDecimal salaireDemande;

    @Column(name = "date_reponse_candidat")
    private LocalDateTime dateReponseCandidat;

    @ManyToOne
    @JoinColumn(name = "id_decision_rh")
    private DecisionRh decisionRh;

    @Column(name = "commentaire_rh")
    private String commentaireRh;

    @Column(name = "date_decision_rh")
    private LocalDateTime dateDecisionRh;

    @Column(name = "salaire_final")
    private BigDecimal salaireFinal;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_statut_proposition")
    private StatutProposition statut;
}