package com.grh.controller;

import com.grh.controller.crud.Champ;
import com.grh.controller.crud.CrudController;
import com.grh.controller.crud.Ecran;
import com.grh.controller.crud.Groupe;
import com.grh.dao.VilleDao;
import com.grh.model.Pays;
import com.grh.model.Ville;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/referentiels/villes")
public class VilleController extends CrudController<Ville> {

    public VilleController(VilleDao dao) {
        super(dao, Ville::new,
                new Ecran("/referentiels/villes", "Villes", "ville", "fa-city", Groupe.REFERENTIELS, 13),
                List.of(
                        Champ.texte("nomVille", "Ville").obligatoire().enListe(),
                        Champ.relation("pays", "Pays", Pays.class).obligatoire().enListe()
                ));
    }
}
