package com.grh.controller;

import com.grh.controller.crud.Champ;
import com.grh.controller.crud.CrudController;
import com.grh.controller.crud.Ecran;
import com.grh.controller.crud.Groupe;
import com.grh.dao.NiveauCompetenceDao;
import com.grh.model.NiveauCompetence;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/referentiels/niveaux-competence")
public class NiveauCompetenceController extends CrudController<NiveauCompetence> {

    public NiveauCompetenceController(NiveauCompetenceDao dao) {
        super(dao, NiveauCompetence::new,
                new Ecran("/referentiels/niveaux-competence", "Niveaux de compétence", "niveau de compétence", "fa-signal", Groupe.REFERENTIELS, 17),
                List.of(
                        Champ.texte("libelleNiveau", "Niveau").obligatoire().enListe()
                ));
    }
}
