package com.grh.config;

import com.grh.controller.crud.CrudController;
import com.grh.controller.crud.Ecran;
import com.grh.controller.crud.Groupe;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Menu principal construit à partir des écrans CRUD déclarés.
 * Accessible dans les templates via ${@navigation.rubriques}.
 */
@Component
public class Navigation {

    public record Lien(Ecran ecran, CrudController<?> controleur) {
    }

    public record Rubrique(Groupe groupe, List<Lien> liens) {
    }

    private final List<CrudController<?>> controleurs;

    public Navigation(@Lazy List<CrudController<?>> controleurs) {
        this.controleurs = controleurs;
    }

    public List<Rubrique> getRubriques() {
        return Arrays.stream(Groupe.values())
                .map(g -> new Rubrique(g, controleurs.stream()
                        .filter(c -> c.getVue().getEcran().groupe() == g)
                        .sorted(Comparator.comparingInt(c -> c.getVue().getEcran().ordre()))
                        .map(c -> new Lien(c.getVue().getEcran(), c))
                        .toList()))
                .filter(r -> !r.liens().isEmpty())
                .toList();
    }

    /** Vrai sur la liste de l'écran et sur ses formulaires (/nouveau, /{id}, /enregistrer). */
    public boolean estActif(String uri, Ecran ecran) {
        return uri != null && uri.matches(java.util.regex.Pattern.quote(ecran.base()) + "(/([0-9]+|nouveau|enregistrer))?/?");
    }

    public boolean estRubriqueActive(String uri, Rubrique rubrique) {
        return rubrique.liens().stream().anyMatch(l -> estActif(uri, l.ecran()));
    }
}
