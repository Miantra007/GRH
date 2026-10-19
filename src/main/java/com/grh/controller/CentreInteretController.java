package com.grh.controller;

import com.grh.controller.crud.Champ;
import com.grh.controller.crud.CrudController;
import com.grh.controller.crud.Ecran;
import com.grh.controller.crud.Groupe;
import com.grh.dao.CentreInteretDao;
import com.grh.model.CentreInteret;
import com.grh.model.Cv;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/cv/centres-interet")
public class CentreInteretController extends CrudController<CentreInteret> {

    public CentreInteretController(CentreInteretDao dao) {
        super(dao, CentreInteret::new,
                new Ecran("/cv/centres-interet", "Centres d'intérêt", "centre d'intérêt", "fa-heart", Groupe.RECRUTEMENT, 8),
                List.of(
                        Champ.relation("cv", "CV", Cv.class).obligatoire().enListe(),
                        Champ.texte("nomCentreInteret", "Centre d'intérêt").obligatoire().enListe(),
                        Champ.texteLong("description", "Description")
                ));
    }
}
