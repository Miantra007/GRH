package com.grh.controller;

import com.grh.controller.crud.Champ;
import com.grh.controller.crud.CrudController;
import com.grh.controller.crud.Ecran;
import com.grh.controller.crud.Groupe;
import com.grh.dao.CvDao;
import com.grh.model.Candidat;
import com.grh.model.Cv;
import com.grh.model.Ville;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/cv")
public class CvController extends CrudController<Cv> {

    public CvController(CvDao dao) {
        super(dao, Cv::new,
                new Ecran("/cv", "CV", "CV", "fa-file-lines", Groupe.RECRUTEMENT, 2),
                List.of(
                        Champ.relation("candidat", "Candidat", Candidat.class).obligatoire().enListe(),
                        Champ.texte("titreCv", "Titre du CV").enListe(),
                        Champ.texteLong("profilProfessionnel", "Profil professionnel"),
                        Champ.texte("adresse", "Adresse"),
                        Champ.relation("ville", "Ville", Ville.class).enListe(),
                        Champ.texteLong("photoUrl", "URL de la photo"),
                        Champ.texteLong("linkedinUrl", "LinkedIn"),
                        Champ.texteLong("githubUrl", "GitHub"),
                        Champ.dateHeure("dateCreation", "Créé le").horsFormulaire().enListe()
                ));
    }
}
