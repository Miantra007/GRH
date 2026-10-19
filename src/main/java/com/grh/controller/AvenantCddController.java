package com.grh.controller;

import com.grh.controller.crud.Champ;
import com.grh.controller.crud.CrudController;
import com.grh.controller.crud.Ecran;
import com.grh.controller.crud.Groupe;
import com.grh.dao.AvenantCddDao;
import com.grh.model.AvenantCdd;
import com.grh.model.ContratCdd;
import com.grh.model.TypeAvenant;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/contrats/cdd/avenants")
public class AvenantCddController extends CrudController<AvenantCdd> {

    public AvenantCddController(AvenantCddDao dao) {
        super(dao, AvenantCdd::new,
                new Ecran("/contrats/cdd/avenants", "Avenants CDD", "avenant", "fa-file-pen", Groupe.CONTRATS, 5),
                List.of(
                        Champ.relation("contrat", "Contrat CDD", ContratCdd.class).obligatoire().enListe(),
                        Champ.relation("typeAvenant", "Type d'avenant", TypeAvenant.class).obligatoire().enListe(),
                        Champ.entier("numero", "Numéro").obligatoire().enListe(),
                        Champ.date("dateAvenant", "Date de l'avenant").obligatoire().enListe(),
                        Champ.date("nouvelleDateFin", "Nouvelle date de fin").enListe(),
                        Champ.decimal("nouveauSalaire", "Nouveau salaire", "1"),
                        Champ.texteLong("objet", "Objet")
                ));
    }
}
