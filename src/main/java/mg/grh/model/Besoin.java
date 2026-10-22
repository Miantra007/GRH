package mg.grh.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDateTime;

@Entity
@Table(name = "besoin")
@DynamicInsert
@Getter
@Setter
@NoArgsConstructor
public class Besoin implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_besoin")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_direction", nullable = false)
    private Direction direction;

    @ManyToOne
    @JoinColumn(name = "id_dept", nullable = false)
    private Departement departement;

    @ManyToOne
    @JoinColumn(name = "id_directeur_demandeur", nullable = false)
    private Responsable directeurDemandeur;

    @ManyToOne
    @JoinColumn(name = "id_type_besoin", nullable = false)
    private TypeBesoin typeBesoin;

    @Column(name = "motif")
    private String motif;

    @ManyToOne
    @JoinColumn(name = "id_statut_besoin", nullable = false)
    private StatutBesoin statutBesoin;

    @ManyToOne
    @JoinColumn(name = "id_rh_traitant")
    private Responsable rhTraitant;

    @ManyToOne
    @JoinColumn(name = "id_finance_validateur")
    private Responsable financeValidateur;

    @ManyToOne
    @JoinColumn(name = "id_dg_validateur")
    private Responsable dgValidateur;

    @Column(name = "date_demande", nullable = false)
    private LocalDateTime dateDemande;

    @Column(name = "date_traitement_rh")
    private LocalDateTime dateTraitementRh;

    @Column(name = "date_validation_finance")
    private LocalDateTime dateValidationFinance;

    @Column(name = "date_validation_dg")
    private LocalDateTime dateValidationDg;

    @Column(name = "date_publication_client")
    private LocalDateTime datePublicationClient;

    @Column(name = "commentaire_rh")
    private String commentaireRh;

    @Column(name = "commentaire_finance")
    private String commentaireFinance;

    @Column(name = "commentaire_dg")
    private String commentaireDg;

    @Column(name = "visible_client", nullable = false)
    private Boolean visibleClient = false;
}
