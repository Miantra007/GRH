package com.grh.controller;

import com.grh.controller.crud.Champ;
import com.grh.controller.crud.CrudController;
import com.grh.controller.crud.Ecran;
import com.grh.controller.crud.Groupe;
import com.grh.dao.CategorieProfessionnelleDao;
import com.grh.model.CategorieProfessionnelle;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/referentiels/categories")
public class CategorieProfessionnelleController extends CrudController<CategorieProfessionnelle> {

    public CategorieProfessionnelleController(CategorieProfessionnelleDao dao) {
        super(dao, CategorieProfessionnelle::new,
                new Ecran("/referentiels/categories", "Catégories professionnelles", "catégorie", "fa-layer-group", Groupe.REFERENTIELS, 2),
                List.of(
                        Champ.texte("code", "Code").obligatoire().enListe(),
                        Champ.texte("libelle", "Libellé").obligatoire().enListe(),
                        Champ.texte("indice", "Indice").enListe()
                ));
    }
}
