package com.grh.controller;

import com.grh.controller.crud.Champ;
import com.grh.controller.crud.CrudController;
import com.grh.controller.crud.Ecran;
import com.grh.controller.crud.Groupe;
import com.grh.dao.DepartementDao;
import com.grh.model.Departement;
import com.grh.model.Responsable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/departements")
public class DepartementController extends CrudController<Departement> {

    public DepartementController(DepartementDao dao) {
        super(dao, Departement::new,
                new Ecran("/departements", "Départements", "département", "fa-sitemap", Groupe.ORGANISATION, 1),
                List.of(
                        Champ.texte("nomDept", "Nom du département").obligatoire().enListe(),
                        Champ.relation("responsable", "Responsable", Responsable.class).enListe()
                ));
    }
}
