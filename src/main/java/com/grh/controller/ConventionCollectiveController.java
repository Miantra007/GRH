package com.grh.controller;

import com.grh.controller.crud.Champ;
import com.grh.controller.crud.CrudController;
import com.grh.controller.crud.Ecran;
import com.grh.controller.crud.Groupe;
import com.grh.dao.ConventionCollectiveDao;
import com.grh.model.ConventionCollective;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/referentiels/conventions")
public class ConventionCollectiveController extends CrudController<ConventionCollective> {

    public ConventionCollectiveController(ConventionCollectiveDao dao) {
        super(dao, ConventionCollective::new,
                new Ecran("/referentiels/conventions", "Conventions collectives", "convention", "fa-book", Groupe.REFERENTIELS, 3),
                List.of(
                        Champ.texte("libelle", "Libellé").obligatoire().enListe()
                ));
    }
}
