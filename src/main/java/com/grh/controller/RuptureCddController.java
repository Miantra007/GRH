package com.grh.controller;

import com.grh.controller.crud.Champ;
import com.grh.controller.crud.CrudController;
import com.grh.controller.crud.Ecran;
import com.grh.controller.crud.Groupe;
import com.grh.dao.RuptureCddDao;
import com.grh.model.ContratCdd;
import com.grh.model.RuptureCdd;
import com.grh.model.TypeRupture;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/contrats/cdd/ruptures")
public class RuptureCddController extends CrudController<RuptureCdd> {

    public RuptureCddController(RuptureCddDao dao) {
        super(dao, RuptureCdd::new,
                new Ecran("/contrats/cdd/ruptures", "Ruptures CDD", "rupture", "fa-file-circle-xmark", Groupe.CONTRATS, 6),
                List.of(
                        Champ.relation("contrat", "Contrat CDD", ContratCdd.class).obligatoire().enListe(),
                        Champ.relation("typeRupture", "Type de rupture", TypeRupture.class).obligatoire().enListe(),
                        Champ.date("dateRupture", "Date de rupture").obligatoire().enListe(),
                        Champ.texte("motif", "Motif").enListe(),
                        Champ.texteLong("observations", "Observations")
                ));
    }
}
