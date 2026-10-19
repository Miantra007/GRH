package com.grh.controller.crud;

/** Rubriques du menu principal, dans l'ordre d'affichage. */
public enum Groupe {
    CONTRATS("Contrats", "fa-file-contract"),
    PARTIES("Parties", "fa-handshake"),
    ORGANISATION("Organisation", "fa-sitemap"),
    RECRUTEMENT("Recrutement", "fa-user-graduate"),
    REFERENTIELS("Référentiels", "fa-list-check");

    private final String libelle;
    private final String icone;

    Groupe(String libelle, String icone) {
        this.libelle = libelle;
        this.icone = icone;
    }

    public String getLibelle() {
        return libelle;
    }

    public String getIcone() {
        return icone;
    }
}
