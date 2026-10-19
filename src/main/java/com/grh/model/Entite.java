package com.grh.model;

/**
 * Contrat commun à toutes les entités : un identifiant entier
 * et un libellé lisible pour les listes déroulantes et les tableaux.
 */
public interface Entite {

    Integer getId();

    String getAffichage();
}
