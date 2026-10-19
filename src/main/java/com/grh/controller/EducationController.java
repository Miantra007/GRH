package com.grh.controller;

import com.grh.controller.crud.Champ;
import com.grh.controller.crud.CrudController;
import com.grh.controller.crud.Ecran;
import com.grh.controller.crud.Groupe;
import com.grh.dao.EducationDao;
import com.grh.model.Cv;
import com.grh.model.Education;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/cv/educations")
public class EducationController extends CrudController<Education> {

    public EducationController(EducationDao dao) {
        super(dao, Education::new,
                new Ecran("/cv/educations", "Éducation", "diplôme", "fa-graduation-cap", Groupe.RECRUTEMENT, 5),
                List.of(
                        Champ.relation("cv", "CV", Cv.class).obligatoire().enListe(),
                        Champ.texte("diplome", "Diplôme").obligatoire().enListe(),
                        Champ.texte("etablissement", "Établissement").enListe(),
                        Champ.texte("domaineEtude", "Domaine d'étude").enListe(),
                        Champ.texte("niveau", "Niveau").enListe(),
                        Champ.date("dateDebut", "Début"),
                        Champ.date("dateFin", "Fin").enListe(),
                        Champ.texteLong("description", "Description")
                ));
    }
}
