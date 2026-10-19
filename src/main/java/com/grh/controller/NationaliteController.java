package com.grh.controller;

import com.grh.controller.crud.Champ;
import com.grh.controller.crud.CrudController;
import com.grh.controller.crud.Ecran;
import com.grh.controller.crud.Groupe;
import com.grh.dao.NationaliteDao;
import com.grh.model.Nationalite;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/referentiels/nationalites")
public class NationaliteController extends CrudController<Nationalite> {

    public NationaliteController(NationaliteDao dao) {
        super(dao, Nationalite::new,
                new Ecran("/referentiels/nationalites", "Nationalités", "nationalité", "fa-flag", Groupe.REFERENTIELS, 11),
                List.of(
                        Champ.texte("libelle", "Libellé").obligatoire().enListe()
                ));
    }
}
