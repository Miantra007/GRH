package mg.grh.dto;

/**
 * Ce que la validation d'un contrat va faire, affiché avant de valider.
 *
 * @param lie         le contrat répond à un besoin
 * @param type        CREATION ou REMPLACEMENT (null si non lié)
 * @param poste       poste concerné
 * @param departement département concerné
 * @param message     explication affichable
 * @param bloque      la validation échouerait (données incohérentes) : le message dit quoi corriger
 */
public record ApercuEmbauche(boolean lie, String type, String poste, String departement, String message, boolean bloque) {

    public static ApercuEmbauche nonLie() {
        return new ApercuEmbauche(false, null, null, null,
                "Aucun besoin n'est lié à ce contrat : sa validation ne modifiera pas la liste des employés.", false);
    }
}
