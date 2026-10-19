package com.grh.controller;

import com.grh.controller.crud.Champ;
import com.grh.controller.crud.CrudController;
import com.grh.controller.crud.Ecran;
import com.grh.controller.crud.Groupe;
import com.grh.dao.SituationFamilleDao;
import com.grh.model.SituationFamille;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/referentiels/situations-famille")
public class SituationFamilleController extends CrudController<SituationFamille> {

    public SituationFamilleController(SituationFamilleDao dao) {
        super(dao, SituationFamille::new,
                new Ecran("/referentiels/situations-famille", "Situations de famille", "situation de famille", "fa-people-roof", Groupe.REFERENTIELS, 10),
                List.of(
                        Champ.texte("libelle", "Libellé").obligatoire().enListe()
                ));
    }
}
