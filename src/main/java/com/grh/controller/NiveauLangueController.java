package com.grh.controller;

import com.grh.controller.crud.Champ;
import com.grh.controller.crud.CrudController;
import com.grh.controller.crud.Ecran;
import com.grh.controller.crud.Groupe;
import com.grh.dao.NiveauLangueDao;
import com.grh.model.NiveauLangue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/referentiels/niveaux-langue")
public class NiveauLangueController extends CrudController<NiveauLangue> {

    public NiveauLangueController(NiveauLangueDao dao) {
        super(dao, NiveauLangue::new,
                new Ecran("/referentiels/niveaux-langue", "Niveaux de langue", "niveau de langue", "fa-signal", Groupe.REFERENTIELS, 16),
                List.of(
                        Champ.texte("libelleNiveau", "Niveau").obligatoire().enListe()
                ));
    }
}
