package com.grh.controller;

import com.grh.controller.crud.Champ;
import com.grh.controller.crud.CrudController;
import com.grh.controller.crud.Ecran;
import com.grh.controller.crud.Groupe;
import com.grh.dao.CvLangueDao;
import com.grh.model.Cv;
import com.grh.model.CvLangue;
import com.grh.model.Langue;
import com.grh.model.NiveauLangue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/cv/langues")
public class CvLangueController extends CrudController<CvLangue> {

    public CvLangueController(CvLangueDao dao) {
        super(dao, CvLangue::new,
                new Ecran("/cv/langues", "Langues des CV", "langue de CV", "fa-comments", Groupe.RECRUTEMENT, 7),
                List.of(
                        Champ.relation("cv", "CV", Cv.class).obligatoire().enListe(),
                        Champ.relation("langue", "Langue", Langue.class).obligatoire().enListe(),
                        Champ.relation("niveauLangue", "Niveau", NiveauLangue.class).obligatoire().enListe()
                ));
    }
}
