package com.grh.controller;

import com.grh.controller.crud.Champ;
import com.grh.controller.crud.CrudController;
import com.grh.controller.crud.Ecran;
import com.grh.controller.crud.Groupe;
import com.grh.dao.CompetenceDao;
import com.grh.model.Competence;
import com.grh.model.Cv;
import com.grh.model.NiveauCompetence;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/cv/competences")
public class CompetenceController extends CrudController<Competence> {

    public CompetenceController(CompetenceDao dao) {
        super(dao, Competence::new,
                new Ecran("/cv/competences", "Compétences", "compétence", "fa-star", Groupe.RECRUTEMENT, 6),
                List.of(
                        Champ.relation("cv", "CV", Cv.class).obligatoire().enListe(),
                        Champ.texte("nomCompetence", "Compétence").obligatoire().enListe(),
                        Champ.relation("niveauCompetence", "Niveau", NiveauCompetence.class).obligatoire().enListe()
                ));
    }
}
