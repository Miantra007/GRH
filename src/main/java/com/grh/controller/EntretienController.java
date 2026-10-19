package com.grh.controller;

import com.grh.controller.crud.Champ;
import com.grh.controller.crud.CrudController;
import com.grh.controller.crud.Ecran;
import com.grh.controller.crud.Groupe;
import com.grh.dao.EntretienDao;
import com.grh.model.Candidat;
import com.grh.model.Entretien;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/entretiens")
public class EntretienController extends CrudController<Entretien> {

    public EntretienController(EntretienDao dao) {
        super(dao, Entretien::new,
                new Ecran("/entretiens", "Entretiens", "entretien", "fa-handshake", Groupe.RECRUTEMENT, 10),
                List.of(
                        Champ.relation("candidat", "Candidat", Candidat.class).obligatoire().enListe(),
                        Champ.texte("description", "Description").enListe(),
                        Champ.date("dateEntretien", "Date de l'entretien").enListe()
                ));
    }
}
