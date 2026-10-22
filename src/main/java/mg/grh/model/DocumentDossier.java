package mg.grh.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "document_dossier")
@Getter @Setter @NoArgsConstructor
public class DocumentDossier implements Identifiable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_document")
    private Integer id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_dossier", nullable = false)
    private DossierCandidature dossier;
    @Column(name = "nom_fichier", nullable = false, length = 255)
    private String nomFichier;
    @Column(name = "chemin_fichier", nullable = false, length = 500)
    private String cheminFichier;
    @Column(name = "type_contenu", length = 120)
    private String typeContenu;
    @Column(name = "taille_octets", nullable = false)
    private long tailleOctets;
}
