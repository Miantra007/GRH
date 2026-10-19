package com.grh.controller;

import com.grh.controller.crud.Champ;
import com.grh.controller.crud.CrudController;
import com.grh.controller.crud.Ecran;
import com.grh.controller.crud.Groupe;
import com.grh.dao.TestTechniqueDao;
import com.grh.model.Candidat;
import com.grh.model.TestTechnique;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/tests-techniques")
public class TestTechniqueController extends CrudController<TestTechnique> {

    public TestTechniqueController(TestTechniqueDao dao) {
        super(dao, TestTechnique::new,
                new Ecran("/tests-techniques", "Tests techniques", "test technique", "fa-laptop-code", Groupe.RECRUTEMENT, 9),
                List.of(
                        Champ.relation("candidat", "Candidat", Candidat.class).obligatoire().enListe(),
                        Champ.texte("description", "Description").enListe(),
                        Champ.date("dateTest", "Date du test").enListe()
                ));
    }
}
