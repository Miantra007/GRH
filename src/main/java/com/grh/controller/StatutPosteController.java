package com.grh.controller;

import com.grh.controller.crud.Champ;
import com.grh.controller.crud.CrudController;
import com.grh.controller.crud.Ecran;
import com.grh.controller.crud.Groupe;
import com.grh.dao.StatutPosteDao;
import com.grh.model.StatutPoste;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/referentiels/statuts-poste")
public class StatutPosteController extends CrudController<StatutPoste> {

    public StatutPosteController(StatutPosteDao dao) {
        super(dao, StatutPoste::new,
                new Ecran("/referentiels/statuts-poste", "Statuts de poste", "statut de poste", "fa-toggle-on", Groupe.REFERENTIELS, 14),
                List.of(
                        Champ.texte("libelle", "Libellé").obligatoire().enListe()
                ));
    }
}
