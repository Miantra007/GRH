package com.grh.model.enums;

/** Les 4 signataires d'un CDI (contrainte CHECK de signature_cdi.role). */
public enum RoleSignataire implements Libelle {
    EMPLOYEUR("L'employeur", "fa-building"),
    TRAVAILLEUR("Le travailleur", "fa-user"),
    DIRECTEUR_REGIONAL("Le directeur régional du travail", "fa-landmark"),
    MEDECIN("Le médecin", "fa-user-doctor");

    private final String libelle;
    private final String icone;

    RoleSignataire(String libelle, String icone) {
        this.libelle = libelle;
        this.icone = icone;
    }

    @Override
    public String getLibelle() {
        return libelle;
    }

    public String getIcone() {
        return icone;
    }
}
