package com.grh.controller;

import com.grh.controller.crud.Champ;
import com.grh.controller.crud.CrudController;
import com.grh.controller.crud.Ecran;
import com.grh.controller.crud.Groupe;
import com.grh.dao.EmployeDao;
import com.grh.model.Employe;
import com.grh.model.PosteFille;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/employes")
public class EmployeController extends CrudController<Employe> {

    public EmployeController(EmployeDao dao) {
        super(dao, Employe::new,
                new Ecran("/employes", "Employés", "employé", "fa-users", Groupe.ORGANISATION, 4),
                List.of(
                        Champ.texte("nomEmploye", "Nom").obligatoire().enListe(),
                        Champ.relation("posteFille", "Poste", PosteFille.class).enListe(),
                        Champ.date("dateEmbauche", "Date d'embauche").enListe()
                ));
    }
}
