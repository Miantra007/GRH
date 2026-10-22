package mg.grh.model;

/** Besoin de recrutement auquel répond un contrat (lecture seule, requête native). */
public interface BesoinInfo {
    Integer getIdBesoin();
    /** CREATION | REMPLACEMENT */
    String getTypeBesoin();
    Integer getIdPoste();
    String getLibellePoste();
    Integer getIdDept();
    String getNomDept();
}
