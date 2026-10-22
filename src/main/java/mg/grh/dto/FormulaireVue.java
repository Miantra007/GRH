package mg.grh.dto;

// dto/FormulaireVue.java

import java.util.List;

public record FormulaireVue(Integer idProposition, Integer idCandidat, String nomComplet,
        String titreOffre, String statutLibelle, boolean modifiable,
        boolean attenteRh, String commentaireRh, List<CritereLigne> lignes) {
}
