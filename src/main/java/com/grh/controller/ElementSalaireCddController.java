package com.grh.controller;

import com.grh.controller.crud.Champ;
import com.grh.controller.crud.CrudController;
import com.grh.controller.crud.Ecran;
import com.grh.controller.crud.Groupe;
import com.grh.dao.ElementSalaireCddDao;
import com.grh.model.ContratCdd;
import com.grh.model.ElementSalaireCdd;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/contrats/cdd/elements-salaire")
public class ElementSalaireCddController extends CrudController<ElementSalaireCdd> {

    public ElementSalaireCddController(ElementSalaireCddDao dao) {
        super(dao, ElementSalaireCdd::new,
                new Ecran("/contrats/cdd/elements-salaire", "Éléments de salaire CDD", "élément de salaire", "fa-coins", Groupe.CONTRATS, 4),
                List.of(
                        Champ.relation("contrat", "Contrat CDD", ContratCdd.class).obligatoire().enListe(),
                        Champ.texte("libelle", "Libellé").obligatoire().enListe(),
                        Champ.decimal("montant", "Montant", "1").obligatoire().enListe(),
                        Champ.texte("nature", "Nature").enListe()
                ));
    }
}
