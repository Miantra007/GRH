package com.grh.controller;

import com.grh.controller.crud.Champ;
import com.grh.controller.crud.CrudController;
import com.grh.controller.crud.Ecran;
import com.grh.controller.crud.Groupe;
import com.grh.dao.LangueDao;
import com.grh.model.Langue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/referentiels/langues")
public class LangueController extends CrudController<Langue> {

    public LangueController(LangueDao dao) {
        super(dao, Langue::new,
                new Ecran("/referentiels/langues", "Langues", "langue", "fa-language", Groupe.REFERENTIELS, 15),
                List.of(
                        Champ.texte("nomLangue", "Langue").obligatoire().enListe()
                ));
    }
}
