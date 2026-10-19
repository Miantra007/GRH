package com.grh.controller.crud;

import com.grh.model.Entite;
import com.grh.model.enums.Libelle;
import org.springframework.beans.BeanWrapperImpl;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;

/**
 * Métadonnées d'un écran + formatage des valeurs, exposé aux templates sous le nom "vue".
 */
public class CrudVue {

    private static final DateTimeFormatter DATE_FR = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DATE_HEURE_FR = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final Ecran ecran;
    private final List<Champ> champs;

    public CrudVue(Ecran ecran, List<Champ> champs) {
        this.ecran = ecran;
        this.champs = champs;
    }

    public Ecran getEcran() {
        return ecran;
    }

    public String getBase() {
        return ecran.base();
    }

    public List<Champ> getChamps() {
        return champs;
    }

    public List<Champ> getColonnes() {
        return champs.stream().filter(Champ::isEnListe).toList();
    }

    public List<Champ> getChampsFormulaire() {
        return champs.stream().filter(Champ::isDansFormulaire).toList();
    }

    public Object brut(Object objet, Champ champ) {
        return new BeanWrapperImpl(objet).getPropertyValue(champ.getNom());
    }

    /** Valeur lisible pour les tableaux. */
    public String afficher(Object objet, Champ champ) {
        Object v = brut(objet, champ);
        if (v == null) {
            return "";
        }
        if (v instanceof Entite e) {
            return e.getAffichage();
        }
        if (v instanceof Libelle l) {
            return l.getLibelle();
        }
        if (v instanceof Boolean b) {
            return b ? "Oui" : "Non";
        }
        if (v instanceof LocalDate d) {
            return d.format(DATE_FR);
        }
        if (v instanceof LocalDateTime d) {
            return d.format(DATE_HEURE_FR);
        }
        if (v instanceof BigDecimal n) {
            return NumberFormat.getNumberInstance(Locale.FRANCE).format(n);
        }
        return v.toString();
    }

    /** Valeur pour l'attribut value d'un champ de formulaire. */
    public String valeur(Object objet, Champ champ) {
        if (champ.getType() == TypeChamp.MOT_DE_PASSE) {
            return "";
        }
        Object v = brut(objet, champ);
        if (v == null) {
            return "";
        }
        if (v instanceof Entite e) {
            return champ.getType() == TypeChamp.AUTOCOMPLETE ? e.getAffichage() : String.valueOf(e.getId());
        }
        if (v instanceof Enum<?> e) {
            return e.name();
        }
        if (v instanceof LocalDateTime d) {
            return d.truncatedTo(ChronoUnit.MINUTES).toString();
        }
        if (v instanceof BigDecimal n) {
            return n.stripTrailingZeros().toPlainString();
        }
        return v.toString();
    }

    public boolean estCoche(Object objet, Champ champ) {
        return Boolean.TRUE.equals(brut(objet, champ));
    }
}
