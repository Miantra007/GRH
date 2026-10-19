package com.grh.controller;

import com.grh.controller.crud.Champ;
import com.grh.controller.crud.CrudController;
import com.grh.controller.crud.Ecran;
import com.grh.controller.crud.Groupe;
import com.grh.dao.ContratCddDao;
import com.grh.dao.StatutContratDao;
import com.grh.model.CategorieProfessionnelle;
import com.grh.model.ContratCdd;
import com.grh.model.ConventionCollective;
import com.grh.model.Employeur;
import com.grh.model.MotifCdd;
import com.grh.model.Poste;
import com.grh.model.StatutContrat;
import com.grh.model.Travailleur;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/contrats/cdd")
public class ContratCddController extends CrudController<ContratCdd> {

    private final StatutContratDao statutDao;

    public ContratCddController(ContratCddDao dao, StatutContratDao statutDao) {
        super(dao, ContratCdd::new,
                new Ecran("/contrats/cdd", "Contrats CDD", "contrat CDD", "fa-calendar-days", Groupe.CONTRATS, 3),
                List.of(
                        Champ.texte("reference", "Référence").enListe(),
                        Champ.relation("travailleur", "Travailleur", Travailleur.class).obligatoire().enListe(),
                        Champ.relation("employeur", "Employeur", Employeur.class).obligatoire(),
                        Champ.relation("poste", "Poste", Poste.class).obligatoire().enListe(),
                        Champ.relation("categorie", "Catégorie", CategorieProfessionnelle.class),
                        Champ.relation("convention", "Convention collective", ConventionCollective.class),
                        Champ.relation("motif", "Motif du CDD", MotifCdd.class).obligatoire(),
                        Champ.relation("statut", "Statut", StatutContrat.class).obligatoire().enListe(),
                        Champ.texteLong("motifPrecision", "Précision sur le motif"),
                        Champ.texte("regimeJuridique", "Régime juridique"),
                        Champ.date("dateDebut", "Date de début").obligatoire().enListe(),
                        Champ.date("dateFin", "Date de fin").obligatoire().enListe(),
                        Champ.entier("essaiDuree", "Durée d'essai (jours)"),
                        Champ.date("essaiDateFin", "Fin de la période d'essai"),
                        Champ.texte("essaiResultat", "Résultat de l'essai"),
                        Champ.decimal("dureeHebdoHeures", "Durée hebdomadaire (h)", "0.1"),
                        Champ.texte("horaireTravail", "Horaires de travail"),
                        Champ.entier("joursTravailSemaine", "Jours travaillés par semaine"),
                        Champ.texte("lieuTravail", "Lieu de travail"),
                        Champ.decimal("salaireBase", "Salaire de base", "1").obligatoire().enListe(),
                        Champ.texte("devise", "Devise").obligatoire(),
                        Champ.texte("periodicitePaiement", "Périodicité de paiement"),
                        Champ.texte("indiceClassification", "Indice de classification"),
                        Champ.decimal("congeJoursParMois", "Congés (jours par mois)", "0.1"),
                        Champ.date("dateConclusion", "Date de conclusion"),
                        Champ.texte("lieuConclusion", "Lieu de conclusion"),
                        Champ.entier("nombreExemplaires", "Nombre d'exemplaires"),
                        Champ.date("dateVisaInspection", "Date du visa de l'inspection"),
                        Champ.texte("tribunalCompetent", "Tribunal compétent")
                ));
        this.statutDao = statutDao;
    }

    @Override
    protected ContratCdd nouveau() {
        ContratCdd contrat = super.nouveau();
        contrat.setStatut(statutDao.statutInitial());
        return contrat;
    }

    @Override
    protected void valider(ContratCdd contrat, BindingResult resultat) {
        if (contrat.getDateDebut() != null && contrat.getDateFin() != null
                && contrat.getDateFin().isBefore(contrat.getDateDebut())) {
            resultat.rejectValue("dateFin", "ordre", "La date de fin doit être après la date de début.");
        }
    }
}
