package mg.grh.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BesoinCreationForm {
    private Integer directionId;
    private Integer departementId;
    private Integer typeBesoinId;
    private String motif;
    private List<LignePoste> postes = new ArrayList<>();

    @Getter
    @Setter
    @NoArgsConstructor
    public static class LignePoste {
        private String libelle;
        private Integer nombrePostes;
        private String missions;
        private Integer typeContratId;
        private String horaireTravail;
        private String responsableHierarchique;
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        private LocalDate datePrisePoste;

        public void valider() {
            if (libelle == null || libelle.isBlank() || libelle.trim().length() > 50) {
                throw new IllegalArgumentException("Chaque poste doit avoir un libellé de 50 caractères maximum.");
            }
            if (nombrePostes == null || nombrePostes < 1) {
                throw new IllegalArgumentException("Le nombre de postes doit être supérieur à zéro.");
            }
            if (typeContratId == null) {
                throw new IllegalArgumentException("Sélectionnez un type de contrat pour chaque poste.");
            }
        }
    }
}
