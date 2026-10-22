package mg.grh.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter @NoArgsConstructor
public class BesoinDetailsForm {
    private String commentaireRh;
    private List<Ligne> lignes = new ArrayList<>();

    @Getter @Setter @NoArgsConstructor
    public static class Ligne {
        private Integer besoinPosteId;
        private BigDecimal salaireProposeRh;
        private String titre;
        private String description;
        private String profilRecherche;
        private LocalDate dateLimite;
    }
}
