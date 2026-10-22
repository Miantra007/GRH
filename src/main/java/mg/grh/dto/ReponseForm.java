package mg.grh.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
public class ReponseForm {
    private String action; // VALIDER / ATTENDRE / REFUSER
    private Map<String, String> reponse = new LinkedHashMap<>(); // code critère -> OUI/NON/AUTRE
    private Map<String, String> contre = new LinkedHashMap<>(); // code critère -> proposition du candidat
}