package com.grh.controller;

import com.grh.controller.crud.Champ;
import com.grh.controller.crud.CrudController;
import com.grh.controller.crud.Ecran;
import com.grh.controller.crud.Groupe;
import com.grh.dao.PosteDao;
import com.grh.model.CategorieProfessionnelle;
import com.grh.model.Poste;
import com.grh.model.Responsable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/postes")
public class PosteController extends CrudController<Poste> {

    public PosteController(PosteDao dao) {
        super(dao, Poste::new,
                new Ecran("/postes", "Postes", "poste", "fa-briefcase", Groupe.ORGANISATION, 2),
                List.of(
                        Champ.texte("intitule", "Intitulé").obligatoire().enListe(),
                        Champ.relation("categorie", "Catégorie", CategorieProfessionnelle.class).enListe(),
                        Champ.relation("responsable", "Responsable", Responsable.class).enListe(),
                        Champ.date("dateCreation", "Date de création").enListe()
                ));
    }
}
