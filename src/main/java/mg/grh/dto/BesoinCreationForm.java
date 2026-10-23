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
        /** Poste choisi parmi ceux de la direction du directeur. */
        private Integer posteId;
        private Integer nombrePostes;
        private String missions;
        private String horaireTravail;
        private String responsableHierarchique;
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        private LocalDate datePrisePoste;

        public void valider() {
            if (posteId == null) {
                throw new IllegalArgumentException("Sélectionnez un poste pour chaque ligne.");
            }
            if (nombrePostes == null || nombrePostes < 1) {
                throw new IllegalArgumentException("Le nombre de postes doit être supérieur à zéro.");
            }
        }
    }
}
