package com.grh.controller;

import com.grh.controller.crud.Champ;
import com.grh.controller.crud.CrudController;
import com.grh.controller.crud.Ecran;
import com.grh.controller.crud.Groupe;
import com.grh.dao.MotifCddDao;
import com.grh.model.MotifCdd;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/referentiels/motifs-cdd")
public class MotifCddController extends CrudController<MotifCdd> {

    public MotifCddController(MotifCddDao dao) {
        super(dao, MotifCdd::new,
                new Ecran("/referentiels/motifs-cdd", "Motifs de CDD", "motif", "fa-circle-question", Groupe.REFERENTIELS, 4),
                List.of(
                        Champ.texte("libelle", "Libellé").obligatoire().enListe()
                ));
    }
}
