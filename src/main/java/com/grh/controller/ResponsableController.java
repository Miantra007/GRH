package com.grh.controller;

import com.grh.controller.crud.Champ;
import com.grh.controller.crud.CrudController;
import com.grh.controller.crud.Ecran;
import com.grh.controller.crud.Groupe;
import com.grh.dao.ResponsableDao;
import com.grh.model.Responsable;
import com.grh.model.Role;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/responsables")
public class ResponsableController extends CrudController<Responsable> {

    public ResponsableController(ResponsableDao dao) {
        super(dao, Responsable::new,
                new Ecran("/responsables", "Responsables", "responsable", "fa-user-tie", Groupe.ORGANISATION, 5),
                List.of(
                        Champ.email("email", "E-mail").obligatoire().enListe(),
                        Champ.motDePasse("motDePasse", "Mot de passe").obligatoire(),
                        Champ.relation("role", "Rôle", Role.class).enListe(),
                        Champ.date("dateEmbauche", "Date d'embauche").enListe()
                ));
    }
}
