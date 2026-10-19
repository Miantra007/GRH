package com.grh.controller;

import com.grh.controller.crud.Champ;
import com.grh.controller.crud.CrudController;
import com.grh.controller.crud.Ecran;
import com.grh.controller.crud.Groupe;
import com.grh.dao.TypeRuptureDao;
import com.grh.model.TypeRupture;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/referentiels/types-rupture")
public class TypeRuptureController extends CrudController<TypeRupture> {

    public TypeRuptureController(TypeRuptureDao dao) {
        super(dao, TypeRupture::new,
                new Ecran("/referentiels/types-rupture", "Types de rupture", "type de rupture", "fa-file-circle-xmark", Groupe.REFERENTIELS, 6),
                List.of(
                        Champ.texte("libelle", "Libellé").obligatoire().enListe()
                ));
    }
}
