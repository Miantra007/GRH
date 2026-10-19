package mg.grh.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import java.math.BigDecimal;

@Entity
@Table(name = "employeur")
@DynamicInsert
@Getter
@Setter
@NoArgsConstructor
public class Employeur implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "raison_sociale", length = 100, nullable = false)
    private String raisonSociale;

    @Column(name = "forme_juridique", length = 100)
    private String formeJuridique;

    @Column(name = "capital_social")
    private BigDecimal capitalSocial;

    @Column(name = "adresse_siege", length = 100)
    private String adresseSiege;

    @Column(name = "nif", length = 100)
    private String nif;

    @Column(name = "stat", length = 100)
    private String stat;

    @Column(name = "rcs", length = 100)
    private String rcs;

    @Column(name = "numero_cnaps", length = 100)
    private String numeroCnaps;

    @Column(name = "organisme_sante", length = 100)
    private String organismeSante;

    @Column(name = "numero_organisme_sante", length = 100)
    private String numeroOrganismeSante;

    @Column(name = "telephone", length = 100)
    private String telephone;

    @Column(name = "email", length = 100)
    private String email;

    @ManyToOne
    @JoinColumn(name = "civilite_id")
    private Civilite civilite;

    @Column(name = "representant_nom", length = 100, nullable = false)
    private String representantNom;

    @Column(name = "representant_fonction", length = 100, nullable = false)
    private String representantFonction;
}
