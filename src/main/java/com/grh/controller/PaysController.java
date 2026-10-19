package com.grh.controller;

import com.grh.controller.crud.Champ;
import com.grh.controller.crud.CrudController;
import com.grh.controller.crud.Ecran;
import com.grh.controller.crud.Groupe;
import com.grh.dao.PaysDao;
import com.grh.model.Pays;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/referentiels/pays")
public class PaysController extends CrudController<Pays> {

    public PaysController(PaysDao dao) {
        super(dao, Pays::new,
                new Ecran("/referentiels/pays", "Pays", "pays", "fa-earth-africa", Groupe.REFERENTIELS, 12),
                List.of(
                        Champ.texte("nomPays", "Pays").obligatoire().enListe()
                ));
    }
}
