package com.grh.controller;

import com.grh.controller.crud.Champ;
import com.grh.controller.crud.CrudController;
import com.grh.controller.crud.Ecran;
import com.grh.controller.crud.Groupe;
import com.grh.dao.StatutContratDao;
import com.grh.model.StatutContrat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/referentiels/statuts-contrat")
public class StatutContratController extends CrudController<StatutContrat> {

    public StatutContratController(StatutContratDao dao) {
        super(dao, StatutContrat::new,
                new Ecran("/referentiels/statuts-contrat", "Statuts de contrat", "statut de contrat", "fa-circle-check", Groupe.REFERENTIELS, 1),
                List.of(
                        Champ.texte("libelle", "Libellé").obligatoire().enListe()
                ));
    }
}
