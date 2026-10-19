package com.grh.controller;

import com.grh.controller.crud.Champ;
import com.grh.controller.crud.CrudController;
import com.grh.controller.crud.Ecran;
import com.grh.controller.crud.Groupe;
import com.grh.dao.PosteFilleDao;
import com.grh.model.Departement;
import com.grh.model.Poste;
import com.grh.model.PosteFille;
import com.grh.model.StatutPoste;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/postes-departement")
public class PosteFilleController extends CrudController<PosteFille> {

    public PosteFilleController(PosteFilleDao dao) {
        super(dao, PosteFille::new,
                new Ecran("/postes-departement", "Postes par département", "poste de département", "fa-diagram-project", Groupe.ORGANISATION, 3),
                List.of(
                        Champ.relation("poste", "Poste", Poste.class).obligatoire().enListe(),
                        Champ.relation("departement", "Département", Departement.class).obligatoire().enListe(),
                        Champ.relation("statutPoste", "Statut du poste", StatutPoste.class).enListe()
                ));
    }
}
