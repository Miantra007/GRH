package com.grh.controller;

import com.grh.controller.crud.Champ;
import com.grh.controller.crud.CrudController;
import com.grh.controller.crud.Ecran;
import com.grh.controller.crud.Groupe;
import com.grh.dao.HistoriqueContratEmployeDao;
import com.grh.model.ContratCandidat;
import com.grh.model.Employe;
import com.grh.model.HistoriqueContratEmploye;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/historique-contrats-employes")
public class HistoriqueContratEmployeController extends CrudController<HistoriqueContratEmploye> {

    public HistoriqueContratEmployeController(HistoriqueContratEmployeDao dao) {
        super(dao, HistoriqueContratEmploye::new,
                new Ecran("/historique-contrats-employes", "Historique des contrats employés", "historique de contrat", "fa-clock-rotate-left", Groupe.ORGANISATION, 7),
                List.of(
                        Champ.relation("employe", "Employé", Employe.class).enListe(),
                        Champ.relation("contratCandidat", "Contrat", ContratCandidat.class).enListe(),
                        Champ.date("dateEmbauche", "Date d'embauche").enListe()
                ));
    }
}
