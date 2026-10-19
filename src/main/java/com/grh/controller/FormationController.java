package com.grh.controller;

import com.grh.controller.crud.Champ;
import com.grh.controller.crud.CrudController;
import com.grh.controller.crud.Ecran;
import com.grh.controller.crud.Groupe;
import com.grh.dao.FormationDao;
import com.grh.model.Cv;
import com.grh.model.Formation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/cv/formations")
public class FormationController extends CrudController<Formation> {

    public FormationController(FormationDao dao) {
        super(dao, Formation::new,
                new Ecran("/cv/formations", "Formations", "formation", "fa-chalkboard-user", Groupe.RECRUTEMENT, 4),
                List.of(
                        Champ.relation("cv", "CV", Cv.class).obligatoire().enListe(),
                        Champ.texte("nomFormation", "Formation").obligatoire().enListe(),
                        Champ.texte("etablissement", "Établissement").enListe(),
                        Champ.date("dateDebut", "Début").enListe(),
                        Champ.date("dateFin", "Fin").enListe(),
                        Champ.texteLong("description", "Description")
                ));
    }
}
