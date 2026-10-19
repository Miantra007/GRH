package com.grh.controller.crud;

import com.grh.dao.GenericDao;
import com.grh.model.Entite;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Liste, création, modification et suppression d'une entité.
 * Un contrôleur concret ne fait que décrire son écran et ses champs ;
 * il peut redéfinir les points d'extension (valider, enregistrer, completerFormulaire, vueFormulaire).
 */
public abstract class CrudController<T extends Entite> {

    protected final GenericDao<T> dao;
    private final Supplier<T> fabrique;
    private final CrudVue vue;

    @PersistenceContext
    private EntityManager entityManager;

    protected CrudController(GenericDao<T> dao, Supplier<T> fabrique, Ecran ecran, List<Champ> champs) {
        this.dao = dao;
        this.fabrique = fabrique;
        this.vue = new CrudVue(ecran, champs);
    }

    public CrudVue getVue() {
        return vue;
    }

    public long compter() {
        return dao.compter();
    }

    // ---------- Liaison des paramètres ----------

    @InitBinder("objet")
    public void initBinder(WebDataBinder binder) {
        // "" -> null : les champs facultatifs vides deviennent NULL en base
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
        String[] interdits = Stream.concat(
                Stream.of("id"),
                vue.getChamps().stream().filter(Champ::isLiaisonManuelle).map(Champ::getNom)
        ).toArray(String[]::new);
        binder.setDisallowedFields(interdits);
    }

    /** Objet sur lequel le formulaire est lié : existant si un id est posté, sinon nouveau. */
    @ModelAttribute("objet")
    public T charger(@RequestParam(name = "id", required = false) Integer id) {
        return id == null ? nouveau() : dao.obtenir(id);
    }

    // ---------- Pages ----------

    @GetMapping
    public String lister(Model model) {
        model.addAttribute("objets", dao.listerTous());
        model.addAttribute("vue", vue);
        return "crud/liste";
    }

    @GetMapping("/nouveau")
    public String creer(Model model) {
        return afficherFormulaire(nouveau(), model, Map.of(), List.of());
    }

    @GetMapping("/{id:\\d+}")
    public String modifier(@PathVariable Integer id, Model model) {
        return afficherFormulaire(dao.obtenir(id), model, Map.of(), List.of());
    }

    @PostMapping("/enregistrer")
    public String enregistrer(@ModelAttribute("objet") T objet, BindingResult resultat,
                              HttpServletRequest requete, Model model, RedirectAttributes redirection) {
        boolean creation = objet.getId() == null;
        lierChampsManuels(objet, requete, resultat);
        verifierObligatoires(objet, resultat);
        valider(objet, resultat);

        if (!resultat.hasErrors()) {
            try {
                T enregistre = enregistrer(objet, requete);
                redirection.addFlashAttribute("succes", capitaliser(vue.getEcran().singulier())
                        + (creation ? " créé(e)" : " modifié(e)") + " avec succès.");
                return "redirect:" + vue.getBase() + "/" + enregistre.getId();
            } catch (DataIntegrityViolationException e) {
                resultat.reject("integrite", "Enregistrement refusé par la base : valeur déjà utilisée "
                        + "ou référence invalide.");
            }
        }

        Map<String, String> erreurs = new LinkedHashMap<>();
        for (FieldError fe : resultat.getFieldErrors()) {
            erreurs.putIfAbsent(fe.getField(), fe.isBindingFailure() ? "Valeur invalide." : fe.getDefaultMessage());
        }
        List<String> globales = resultat.getGlobalErrors().stream().map(e -> e.getDefaultMessage()).toList();
        return afficherFormulaire(objet, model, erreurs, globales);
    }

    @PostMapping("/{id:\\d+}/supprimer")
    public String supprimer(@PathVariable Integer id, RedirectAttributes redirection) {
        try {
            dao.supprimer(id);
            redirection.addFlashAttribute("succes", capitaliser(vue.getEcran().singulier()) + " supprimé(e).");
        } catch (DataIntegrityViolationException e) {
            redirection.addFlashAttribute("erreur", "Suppression impossible : cet élément est utilisé ailleurs.");
        }
        return "redirect:" + vue.getBase();
    }

    // ---------- Points d'extension ----------

    protected T nouveau() {
        return fabrique.get();
    }

    /** Règles propres à l'entité, en plus des champs obligatoires. */
    protected void valider(T objet, BindingResult resultat) {
    }

    protected T enregistrer(T objet, HttpServletRequest requete) {
        return dao.enregistrer(objet);
    }

    /** Données supplémentaires pour le formulaire. */
    protected void completerFormulaire(T objet, Model model) {
    }

    protected String vueFormulaire() {
        return "crud/formulaire";
    }

    // ---------- Interne ----------

    private String afficherFormulaire(T objet, Model model, Map<String, String> erreurs, List<String> globales) {
        model.addAttribute("objet", objet);
        model.addAttribute("vue", vue);
        model.addAttribute("options", chargerOptions());
        model.addAttribute("erreurs", erreurs);
        model.addAttribute("erreursGlobales", globales);
        completerFormulaire(objet, model);
        return vueFormulaire();
    }

    private Map<String, List<?>> chargerOptions() {
        Map<String, List<?>> options = new LinkedHashMap<>();
        for (Champ c : vue.getChamps()) {
            if (c.getType() == TypeChamp.RELATION) {
                String jpql = "select e from " + c.getCible().getSimpleName() + " e order by e.id";
                options.put(c.getNom(), entityManager.createQuery(jpql, c.getCible()).getResultList());
            }
        }
        return options;
    }

    private void lierChampsManuels(T objet, HttpServletRequest requete, BindingResult resultat) {
        BeanWrapper bw = new BeanWrapperImpl(objet);
        for (Champ c : vue.getChamps()) {
            String saisie = requete.getParameter(c.getParametre());
            switch (c.getType()) {
                // Mot de passe vide en modification : on garde l'ancien
                case MOT_DE_PASSE -> {
                    if (StringUtils.hasText(saisie)) {
                        bw.setPropertyValue(c.getNom(), saisie);
                    }
                }
                case AUTOCOMPLETE -> bw.setPropertyValue(c.getNom(),
                        StringUtils.hasText(saisie) ? c.getResolveur().apply(saisie.trim()) : null);
                default -> { }
            }
        }
    }

    private void verifierObligatoires(T objet, BindingResult resultat) {
        BeanWrapper bw = new BeanWrapperImpl(objet);
        for (Champ c : vue.getChamps()) {
            if (!c.isObligatoire() || !c.isDansFormulaire() || resultat.hasFieldErrors(c.getNom())) {
                continue;
            }
            Object v = bw.getPropertyValue(c.getNom());
            if (v == null || (v instanceof String s && s.isBlank())) {
                resultat.rejectValue(c.getNom(), "obligatoire", c.getLibelle() + " est obligatoire.");
            }
        }
    }

    protected static String capitaliser(String s) {
        return s.isEmpty() ? s : Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }
}
