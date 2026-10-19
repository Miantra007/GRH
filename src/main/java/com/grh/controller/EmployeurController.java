package com.grh.controller;

import com.grh.controller.crud.Champ;
import com.grh.controller.crud.CrudController;
import com.grh.controller.crud.Ecran;
import com.grh.controller.crud.Groupe;
import com.grh.dao.EmployeurDao;
import com.grh.model.Civilite;
import com.grh.model.Employeur;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/employeurs")
public class EmployeurController extends CrudController<Employeur> {

    public EmployeurController(EmployeurDao dao) {
        super(dao, Employeur::new,
                new Ecran("/employeurs", "Employeurs", "employeur", "fa-building", Groupe.PARTIES, 1),
                List.of(
                        Champ.texte("raisonSociale", "Raison sociale").obligatoire().enListe(),
                        Champ.texte("formeJuridique", "Forme juridique").enListe(),
                        Champ.decimal("capitalSocial", "Capital social (Ar)", "1"),
                        Champ.texte("adresseSiege", "Adresse du siège").enListe(),
                        Champ.texte("nif", "NIF"),
                        Champ.texte("stat", "STAT"),
                        Champ.texte("rcs", "RCS"),
                        Champ.texte("numeroCnaps", "N° CNaPS"),
                        Champ.texte("organismeSante", "Organisme sanitaire"),
                        Champ.texte("numeroOrganismeSante", "N° organisme sanitaire"),
                        Champ.telephone("telephone", "Téléphone").enListe(),
                        Champ.email("email", "E-mail"),
                        Champ.relation("civilite", "Civilité du représentant", Civilite.class),
                        Champ.texte("representantNom", "Nom du représentant").obligatoire().enListe(),
                        Champ.texte("representantFonction", "Fonction du représentant").obligatoire()
                ));
    }
}
