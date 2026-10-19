package com.grh.controller;

import com.grh.controller.crud.Champ;
import com.grh.controller.crud.CrudController;
import com.grh.controller.crud.Ecran;
import com.grh.controller.crud.Groupe;
import com.grh.dao.HistoriqueStatutContratDao;
import com.grh.model.ContratCandidat;
import com.grh.model.HistoriqueStatutContrat;
import com.grh.model.StatutContrat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/contrats/historique-statuts")
public class HistoriqueStatutContratController extends CrudController<HistoriqueStatutContrat> {

    public HistoriqueStatutContratController(HistoriqueStatutContratDao dao) {
        super(dao, HistoriqueStatutContrat::new,
                new Ecran("/contrats/historique-statuts", "Historique des statuts", "historique de statut", "fa-timeline", Groupe.CONTRATS, 9),
                List.of(
                        Champ.relation("contratCandidat", "Contrat", ContratCandidat.class).enListe(),
                        Champ.relation("statutContrat", "Statut", StatutContrat.class).enListe(),
                        Champ.date("date", "Date").enListe()
                ));
    }
}
