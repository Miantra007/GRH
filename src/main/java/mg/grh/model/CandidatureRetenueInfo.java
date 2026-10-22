package mg.grh.model;

/** Candidature dont la proposition d'embauche a reçu l'accord final : un contrat peut être créé pour elle. */
public interface CandidatureRetenueInfo {
    Integer getIdCandidature();
    String getTitreOffre();
    /** CREATION | REMPLACEMENT */
    String getTypeBesoin();
    String getLibellePoste();
    String getNomDept();
}
