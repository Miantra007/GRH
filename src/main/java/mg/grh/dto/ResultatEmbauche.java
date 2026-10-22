package mg.grh.dto;

import mg.grh.model.Employe;

/**
 * Effet de la validation d'un contrat sur la liste des employés.
 *
 * @param type          CREATION, REMPLACEMENT, ou null si le contrat n'est lié à aucun besoin
 * @param message       phrase affichable à l'utilisateur
 * @param nouvelEmploye employé créé (ou retrouvé si la personne était déjà embauchée), sinon null
 * @param ancienEmploye employé remplacé (suspendu et placé dans l'historique), sinon null
 */
public record ResultatEmbauche(String type, String message, Employe nouvelEmploye, Employe ancienEmploye) {

    public static ResultatEmbauche aucun(String message) {
        return new ResultatEmbauche(null, message, null, null);
    }
}
