package com.grh.controller;

import com.grh.controller.crud.Champ;
import com.grh.controller.crud.CrudController;
import com.grh.controller.crud.Ecran;
import com.grh.controller.crud.Groupe;
import com.grh.dao.TypeAvenantDao;
import com.grh.model.TypeAvenant;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/referentiels/types-avenant")
public class TypeAvenantController extends CrudController<TypeAvenant> {

    public TypeAvenantController(TypeAvenantDao dao) {
        super(dao, TypeAvenant::new,
                new Ecran("/referentiels/types-avenant", "Types d'avenant", "type d'avenant", "fa-file-pen", Groupe.REFERENTIELS, 5),
                List.of(
                        Champ.texte("libelle", "Libellé").obligatoire().enListe()
                ));
    }
}
