package com.grh.controller;

import com.grh.controller.crud.Champ;
import com.grh.controller.crud.CrudController;
import com.grh.controller.crud.Ecran;
import com.grh.controller.crud.Groupe;
import com.grh.dao.ContratCeDao;
import com.grh.dao.StatutContratDao;
import com.grh.model.CategorieProfessionnelle;
import com.grh.model.ContratCe;
import com.grh.model.ConventionCollective;
import com.grh.model.Employeur;
import com.grh.model.LangueContrat;
import com.grh.model.StatutContrat;
import com.grh.model.Travailleur;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/contrats/essai")
public class ContratCeController extends CrudController<ContratCe> {

    private final StatutContratDao statutDao;

    public ContratCeController(ContratCeDao dao, StatutContratDao statutDao) {
        super(dao, ContratCe::new,
                new Ecran("/contrats/essai", "Contrats d'essai", "contrat d'essai", "fa-hourglass-half", Groupe.CONTRATS, 7),
                List.of(
                        Champ.relation("travailleur", "Travailleur", Travailleur.class).obligatoire().enListe(),
                        Champ.relation("employeur", "Employeur", Employeur.class).obligatoire(),
                        Champ.relation("statut", "Statut", StatutContrat.class).obligatoire().enListe(),
                        Champ.relation("langue", "Langue du contrat", LangueContrat.class).obligatoire(),
                        Champ.relation("contratInitial", "Renouvellement du contrat", ContratCe.class),
                        Champ.texte("poste", "Poste").enListe(),
                        Champ.relation("categorie", "Catégorie", CategorieProfessionnelle.class).obligatoire(),
                        Champ.texte("indiceClassification", "Indice de classification"),
                        Champ.relation("convention", "Convention collective", ConventionCollective.class),
                        Champ.texteLong("missions", "Missions"),
                        Champ.texte("lieuTravail", "Lieu de travail"),
                        Champ.entier("dureeMois", "Durée (mois)").enListe(),
                        Champ.date("dateDebut", "Date de début").enListe(),
                        Champ.decimal("heuresHebdo", "Heures par semaine", "0.1"),
                        Champ.decimal("remunerationNette", "Rémunération nette (Ar)", "1"),
                        Champ.texte("lieuSignature", "Lieu de signature"),
                        Champ.date("dateSignature", "Date de signature"),
                        Champ.entier("nbExemplaires", "Nombre d'exemplaires")
                ));
        this.statutDao = statutDao;
    }

    @Override
    protected ContratCe nouveau() {
        ContratCe contrat = super.nouveau();
        contrat.setStatut(statutDao.statutInitial());
        return contrat;
    }
}
