package com.grh.controller;

import com.grh.controller.crud.Champ;
import com.grh.controller.crud.CrudController;
import com.grh.controller.crud.Ecran;
import com.grh.controller.crud.Groupe;
import com.grh.dao.ExperienceProfessionnelleDao;
import com.grh.model.Cv;
import com.grh.model.ExperienceProfessionnelle;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/cv/experiences")
public class ExperienceProfessionnelleController extends CrudController<ExperienceProfessionnelle> {

    public ExperienceProfessionnelleController(ExperienceProfessionnelleDao dao) {
        super(dao, ExperienceProfessionnelle::new,
                new Ecran("/cv/experiences", "Expériences professionnelles", "expérience", "fa-briefcase", Groupe.RECRUTEMENT, 3),
                List.of(
                        Champ.relation("cv", "CV", Cv.class).obligatoire().enListe(),
                        Champ.texte("poste", "Poste").obligatoire().enListe(),
                        Champ.texte("entreprise", "Entreprise").enListe(),
                        Champ.texte("typeContrat", "Type de contrat").enListe(),
                        Champ.texte("lieu", "Lieu"),
                        Champ.date("dateDebut", "Début").enListe(),
                        Champ.date("dateFin", "Fin").enListe(),
                        Champ.texteLong("description", "Description")
                ));
    }
}
