package com.grh.controller;

import com.grh.controller.crud.Champ;
import com.grh.controller.crud.CrudController;
import com.grh.controller.crud.Ecran;
import com.grh.controller.crud.Groupe;
import com.grh.dao.NationaliteDao;
import com.grh.dao.TravailleurDao;
import com.grh.model.Civilite;
import com.grh.model.SituationFamille;
import com.grh.model.Travailleur;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/travailleurs")
public class TravailleurController extends CrudController<Travailleur> {

    public TravailleurController(TravailleurDao dao, NationaliteDao nationaliteDao) {
        super(dao, Travailleur::new,
                new Ecran("/travailleurs", "Travailleurs", "travailleur", "fa-id-card", Groupe.PARTIES, 2),
                List.of(
                        Champ.relation("civilite", "Civilité", Civilite.class).enListe(),
                        Champ.texte("nomPrenoms", "Nom et prénoms").obligatoire().enListe(),
                        Champ.date("dateNaissance", "Date de naissance").obligatoire().enListe(),
                        Champ.texte("lieuNaissance", "Lieu de naissance").obligatoire(),
                        Champ.texte("filsDe", "Fils / fille de"),
                        Champ.autocomplete("nationalite", "Nationalité", "/data/pays.json", nationaliteDao::trouverOuCreer)
                                .obligatoire().enListe(),
                        Champ.relation("situationFamille", "Situation de famille", SituationFamille.class),
                        Champ.entier("nombreEnfants", "Nombre d'enfants"),
                        Champ.texte("adresseResidence", "Adresse de résidence"),
                        Champ.texte("profession", "Profession").enListe(),
                        Champ.telephone("telephone", "Téléphone").enListe(),
                        Champ.email("email", "E-mail"),
                        Champ.texte("cinNumero", "N° CIN"),
                        Champ.date("cinDateDelivrance", "CIN délivrée le"),
                        Champ.texte("cinLieuDelivrance", "CIN délivrée à"),
                        Champ.texte("numeroCnaps", "N° CNaPS")
                ));
    }
}
