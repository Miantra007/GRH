package mg.grh.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity @Table(name="proposition_embauche") @Getter @Setter @NoArgsConstructor
public class PropositionEmbauche implements Identifiable {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="id_proposition") private Integer id;
    @OneToOne(fetch=FetchType.EAGER) @JoinColumn(name="id_candidature", nullable=false, unique=true) private Candidature candidature;
    @Column(name="salaire_propose", nullable=false) private BigDecimal salairePropose;
    @Column(name="horaire_travail", length=150) private String horaireTravail;
    @Column(name="responsable_hierarchique", length=150) private String responsableHierarchique;
    @Column(name="travail_a_effectuer") private String travailAEffectuer;
    @Column(name="date_prise_poste") private LocalDate datePrisePoste;
    @Column(name="date_envoi", nullable=false) private LocalDateTime dateEnvoi;
    @ManyToOne @JoinColumn(name="id_reponse_candidat") private ReponseCandidat reponseCandidat;
    @Column(name="commentaire_candidat") private String commentaireCandidat;
    @Column(name="contre_proposition") private String contreProposition;
    @Column(name="salaire_demande") private BigDecimal salaireDemande;
    @Column(name="date_reponse_candidat") private LocalDateTime dateReponseCandidat;
    @ManyToOne @JoinColumn(name="id_decision_rh") private DecisionRh decisionRh;
    @Column(name="commentaire_rh") private String commentaireRh;
    @Column(name="date_decision_rh") private LocalDateTime dateDecisionRh;
    @Column(name="salaire_final") private BigDecimal salaireFinal;
    @ManyToOne @JoinColumn(name="id_statut_proposition", nullable=false) private StatutProposition statutProposition;
}
