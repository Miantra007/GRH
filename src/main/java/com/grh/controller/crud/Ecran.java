package com.grh.controller.crud;

/**
 * Description d'un écran CRUD : URL de base, titres et place dans le menu.
 *
 * @param base      URL de base, ex. "/employeurs"
 * @param titre     titre de la liste, ex. "Employeurs"
 * @param singulier nom d'un élément, ex. "employeur"
 * @param icone     classe Font Awesome, ex. "fa-building"
 * @param groupe    rubrique du menu
 * @param ordre     position dans la rubrique
 */
public record Ecran(String base, String titre, String singulier, String icone, Groupe groupe, int ordre) {
}
