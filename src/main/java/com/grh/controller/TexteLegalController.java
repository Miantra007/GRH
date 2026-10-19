package com.grh.controller;

import com.grh.controller.crud.Champ;
import com.grh.controller.crud.CrudController;
import com.grh.controller.crud.Ecran;
import com.grh.controller.crud.Groupe;
import com.grh.dao.TexteLegalDao;
import com.grh.model.TexteLegal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/referentiels/textes-legaux")
public class TexteLegalController extends CrudController<TexteLegal> {

    public TexteLegalController(TexteLegalDao dao) {
        super(dao, TexteLegal::new,
                new Ecran("/referentiels/textes-legaux", "Textes légaux", "texte légal", "fa-scale-balanced", Groupe.REFERENTIELS, 8),
                List.of(
                        Champ.texte("reference", "Référence").obligatoire().enListe(),
                        Champ.date("dateTexte", "Date").enListe(),
                        Champ.texte("intitule", "Intitulé").obligatoire().enListe()
                ));
    }
}
