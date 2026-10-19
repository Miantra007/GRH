package com.grh.controller;

import com.grh.controller.crud.Champ;
import com.grh.controller.crud.CrudController;
import com.grh.controller.crud.Ecran;
import com.grh.controller.crud.Groupe;
import com.grh.dao.CandidatDao;
import com.grh.model.Candidat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/candidats")
public class CandidatController extends CrudController<Candidat> {

    public CandidatController(CandidatDao dao) {
        super(dao, Candidat::new,
                new Ecran("/candidats", "Candidats", "candidat", "fa-user-graduate", Groupe.RECRUTEMENT, 1),
                List.of(
                        Champ.texte("nomCandidat", "Nom").obligatoire().enListe(),
                        Champ.texte("prenomCandidat", "Prénom").enListe(),
                        Champ.email("email", "E-mail").obligatoire().enListe(),
                        Champ.motDePasse("motDePasse", "Mot de passe").obligatoire(),
                        Champ.date("dtn", "Date de naissance").enListe()
                ));
    }
}
