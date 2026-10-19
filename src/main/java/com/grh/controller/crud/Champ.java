package com.grh.controller.crud;

import com.grh.model.Entite;
import com.grh.model.enums.Libelle;
import lombok.Getter;

import java.util.function.Function;

/**
 * Description d'une propriété d'entité pour les pages génériques (liste + formulaire).
 * Se construit avec les fabriques statiques puis les méthodes fluides :
 * <pre>Champ.texte("raisonSociale", "Raison sociale").obligatoire().enListe()</pre>
 */
@Getter
public class Champ {

    private final String nom;
    private final String libelle;
    private final TypeChamp type;

    private boolean obligatoire;
    private boolean enListe;
    private boolean dansFormulaire = true;
    private boolean pleineLargeur;
    private String aide;
    private String pas;

    /** Entité cible (RELATION, AUTOCOMPLETE) ou énumération (ENUMERATION). */
    private Class<?> cible;

    /** AUTOCOMPLETE : URL de la liste JSON et conversion du texte saisi en entité. */
    private String sourceAutocomplete;
    private Function<String, ? extends Entite> resolveur;

    private Champ(String nom, String libelle, TypeChamp type) {
        this.nom = nom;
        this.libelle = libelle;
        this.type = type;
    }

    // ---------- Fabriques ----------

    public static Champ texte(String nom, String libelle) {
        return new Champ(nom, libelle, TypeChamp.TEXTE);
    }

    public static Champ texteLong(String nom, String libelle) {
        Champ c = new Champ(nom, libelle, TypeChamp.TEXTE_LONG);
        c.pleineLargeur = true;
        return c;
    }

    public static Champ email(String nom, String libelle) {
        return new Champ(nom, libelle, TypeChamp.EMAIL);
    }

    public static Champ telephone(String nom, String libelle) {
        return new Champ(nom, libelle, TypeChamp.TELEPHONE);
    }

    public static Champ motDePasse(String nom, String libelle) {
        return new Champ(nom, libelle, TypeChamp.MOT_DE_PASSE);
    }

    public static Champ entier(String nom, String libelle) {
        return new Champ(nom, libelle, TypeChamp.ENTIER);
    }

    public static Champ decimal(String nom, String libelle, String pas) {
        Champ c = new Champ(nom, libelle, TypeChamp.DECIMAL);
        c.pas = pas;
        return c;
    }

    public static Champ date(String nom, String libelle) {
        return new Champ(nom, libelle, TypeChamp.DATE);
    }

    public static Champ dateHeure(String nom, String libelle) {
        return new Champ(nom, libelle, TypeChamp.DATE_HEURE);
    }

    public static Champ booleen(String nom, String libelle) {
        return new Champ(nom, libelle, TypeChamp.BOOLEEN);
    }

    public static Champ relation(String nom, String libelle, Class<? extends Entite> cible) {
        Champ c = new Champ(nom, libelle, TypeChamp.RELATION);
        c.cible = cible;
        return c;
    }

    public static <E extends Enum<E> & Libelle> Champ enumeration(String nom, String libelle, Class<E> cible) {
        Champ c = new Champ(nom, libelle, TypeChamp.ENUMERATION);
        c.cible = cible;
        return c;
    }

    public static Champ autocomplete(String nom, String libelle, String source,
                                     Function<String, ? extends Entite> resolveur) {
        Champ c = new Champ(nom, libelle, TypeChamp.AUTOCOMPLETE);
        c.sourceAutocomplete = source;
        c.resolveur = resolveur;
        return c;
    }

    // ---------- Options ----------

    public Champ obligatoire() {
        this.obligatoire = true;
        return this;
    }

    public Champ enListe() {
        this.enListe = true;
        return this;
    }

    /** Affiché dans la liste seulement (valeur calculée par la base, ex. date de création). */
    public Champ horsFormulaire() {
        this.dansFormulaire = false;
        return this;
    }

    public Champ aide(String aide) {
        this.aide = aide;
        return this;
    }

    // ---------- Pour les templates ----------

    /** Nom du paramètre HTTP : différent du nom de propriété quand le contrôleur le traite lui-même. */
    public String getParametre() {
        return type == TypeChamp.AUTOCOMPLETE ? nom + "Libelle" : nom;
    }

    /** Champs que le contrôleur traite à la main au lieu de laisser Spring les lier. */
    public boolean isLiaisonManuelle() {
        return type == TypeChamp.MOT_DE_PASSE || type == TypeChamp.AUTOCOMPLETE;
    }

    public Object[] getValeursEnum() {
        return cible != null && cible.isEnum() ? cible.getEnumConstants() : new Object[0];
    }

    public String getTypeHtml() {
        return switch (type) {
            case EMAIL -> "email";
            case TELEPHONE -> "tel";
            case MOT_DE_PASSE -> "password";
            case ENTIER, DECIMAL -> "number";
            case DATE -> "date";
            case DATE_HEURE -> "datetime-local";
            default -> "text";
        };
    }
}
