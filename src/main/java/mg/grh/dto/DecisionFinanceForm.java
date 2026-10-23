package mg.grh.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter @NoArgsConstructor
public class DecisionFinanceForm {
    private String commentaireFinance;
    private List<Ligne> lignes = new ArrayList<>();

    @Getter @Setter @NoArgsConstructor
    public static class Ligne {
        private Integer besoinPosteId;
        private String decision;
        private String commentaire;
        /** Salaire proposé par la Finance quand elle refuse celui du RH : c'est lui qui est retenu. */
        private java.math.BigDecimal salaireFinance;
    }
}
