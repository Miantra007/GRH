package com.grh.controller;

import com.grh.controller.crud.Champ;
import com.grh.controller.crud.CrudController;
import com.grh.controller.crud.Ecran;
import com.grh.controller.crud.Groupe;
import com.grh.dao.CiviliteDao;
import com.grh.model.Civilite;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/referentiels/civilites")
public class CiviliteController extends CrudController<Civilite> {

    public CiviliteController(CiviliteDao dao) {
        super(dao, Civilite::new,
                new Ecran("/referentiels/civilites", "Civilités", "civilité", "fa-venus-mars", Groupe.REFERENTIELS, 9),
                List.of(
                        Champ.texte("libelle", "Libellé").obligatoire().enListe()
                ));
    }
}
