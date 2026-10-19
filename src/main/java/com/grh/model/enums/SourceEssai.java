package com.grh.model.enums;

/** Texte de référence de la période d'essai d'un CDI (contrainte CHECK de contrat_cdi.source_essai). */
public enum SourceEssai implements Libelle {
    CODE_TRAVAIL("Code du travail"),
    CONVENTION_COLLECTIVE("Convention collective"),
    ACCORD_ETABLISSEMENT("Accord d'établissement");

    private final String libelle;

    SourceEssai(String libelle) {
        this.libelle = libelle;
    }

    @Override
    public String getLibelle() {
        return libelle;
    }
}
