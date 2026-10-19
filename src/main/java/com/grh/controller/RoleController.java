package com.grh.controller;

import com.grh.controller.crud.Champ;
import com.grh.controller.crud.CrudController;
import com.grh.controller.crud.Ecran;
import com.grh.controller.crud.Groupe;
import com.grh.dao.RoleDao;
import com.grh.model.Role;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/roles")
public class RoleController extends CrudController<Role> {

    public RoleController(RoleDao dao) {
        super(dao, Role::new,
                new Ecran("/roles", "Rôles", "rôle", "fa-user-tag", Groupe.ORGANISATION, 6),
                List.of(
                        Champ.texte("libelle", "Libellé").obligatoire().enListe()
                ));
    }
}
