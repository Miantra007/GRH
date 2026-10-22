package mg.grh.model;


import java.math.BigDecimal;

public interface PropositionInfo {
    Integer getIdProposition();
    Integer getIdCandidat();
    String getNomCandidat();
    String getPrenomCandidat();
    String getTitreOffre();
    Integer getIdRhTraitant();

    // décidés par le RH lors du besoin (table besoin_poste)
    BigDecimal getSalaireInitial();
    String getHoraireTravail();
    String getResponsableHierarchique();
    String getMissions();
    String getDatePrisePoste();   // déjà formatée jj/mm/aaaa
}