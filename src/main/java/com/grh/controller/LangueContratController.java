package com.grh.controller;

import com.grh.controller.crud.Champ;
import com.grh.controller.crud.CrudController;
import com.grh.controller.crud.Ecran;
import com.grh.controller.crud.Groupe;
import com.grh.dao.LangueContratDao;
import com.grh.model.LangueContrat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/referentiels/langues-contrat")
public class LangueContratController extends CrudController<LangueContrat> {

    public LangueContratController(LangueContratDao dao) {
        super(dao, LangueContrat::new,
                new Ecran("/referentiels/langues-contrat", "Langues de contrat", "langue de contrat", "fa-language", Groupe.REFERENTIELS, 7),
                List.of(
                        Champ.texte("libelle", "Libellé").obligatoire().enListe()
                ));
    }
}
