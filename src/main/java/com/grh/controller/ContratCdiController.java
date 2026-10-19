package com.grh.controller;

import com.grh.controller.crud.Champ;
import com.grh.controller.crud.CrudController;
import com.grh.controller.crud.Ecran;
import com.grh.controller.crud.Groupe;
import com.grh.dao.ContratCdiDao;
import com.grh.dao.StatutContratDao;
import com.grh.dao.TexteLegalDao;
import com.grh.model.CategorieProfessionnelle;
import com.grh.model.ContratCdi;
import com.grh.model.ConventionCollective;
import com.grh.model.Employeur;
import com.grh.model.StatutContrat;
import com.grh.model.Travailleur;
import com.grh.model.enums.RoleSignataire;
import com.grh.model.enums.SourceEssai;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/contrats/cdi")
public class ContratCdiController extends CrudController<ContratCdi> {

    private final ContratCdiDao contratDao;
    private final StatutContratDao statutDao;
    private final TexteLegalDao texteLegalDao;

    public ContratCdiController(ContratCdiDao dao, StatutContratDao statutDao, TexteLegalDao texteLegalDao) {
        super(dao, ContratCdi::new,
                new Ecran("/contrats/cdi", "Contrats CDI", "contrat CDI", "fa-file-contract", Groupe.CONTRATS, 1),
                List.of(
                        Champ.relation("travailleur", "Travailleur", Travailleur.class).obligatoire().enListe(),
                        Champ.relation("employeur", "Employeur", Employeur.class).obligatoire().enListe(),
                        Champ.relation("statut", "Statut", StatutContrat.class).obligatoire().enListe(),
                        Champ.date("datePriseFonction", "Date de prise de fonction").obligatoire().enListe(),
                        Champ.texte("emploi", "Emploi").obligatoire().enListe(),
                        Champ.booleen("nonLieAutreEntreprise", "Non lié à une autre entreprise"),
                        Champ.texte("etablissement", "Établissement"),
                        Champ.texte("region", "Région"),
                        Champ.texte("districtCommune", "District / Commune"),
                        Champ.texte("dureeEssai", "Durée d'essai"),
                        Champ.texte("articleReferenceEssai", "Article de référence (essai)"),
                        Champ.enumeration("sourceEssai", "Source de la période d'essai", SourceEssai.class),
                        Champ.relation("categorie", "Catégorie", CategorieProfessionnelle.class),
                        Champ.relation("convention", "Convention collective", ConventionCollective.class),
                        Champ.decimal("salaireBrutMensuel", "Salaire brut mensuel (Ar)", "1").enListe(),
                        Champ.decimal("dureeHebdoHeures", "Durée hebdomadaire (h)", "0.1"),
                        Champ.texte("horairesFixesPar", "Horaires fixés par"),
                        Champ.texte("lieuRecrutement", "Lieu de recrutement"),
                        Champ.texte("regimeAccidentTravail", "Régime accident du travail"),
                        Champ.texte("regimeMaladieNonPro", "Régime maladie non professionnelle"),
                        Champ.texte("entrepriseConfidentialite", "Entreprise (confidentialité)"),
                        Champ.texte("directionRegionaleTravail", "Direction régionale du travail"),
                        Champ.entier("nbExemplaires", "Nombre d'exemplaires").obligatoire(),
                        Champ.texte("lieuSignature", "Lieu de signature"),
                        Champ.date("dateSignature", "Date de signature")
                ));
        this.contratDao = dao;
        this.statutDao = statutDao;
        this.texteLegalDao = texteLegalDao;
    }

    @Override
    protected ContratCdi nouveau() {
        ContratCdi contrat = super.nouveau();
        contrat.setStatut(statutDao.statutInitial());
        return contrat;
    }

    /** Formulaire dédié reprenant la mise en page du modèle de contrat (Annexe I). */
    @Override
    protected String vueFormulaire() {
        return "contrat/contrat-cdi";
    }

    @Override
    protected void completerFormulaire(ContratCdi contrat, Model model) {
        model.addAttribute("roles", RoleSignataire.values());
        model.addAttribute("signes", contratDao.signataires(contrat));
        model.addAttribute("textesLegaux", texteLegalDao.listerTous());
    }

    @Override
    protected ContratCdi enregistrer(ContratCdi contrat, HttpServletRequest requete) {
        Set<RoleSignataire> signes = EnumSet.noneOf(RoleSignataire.class);
        String[] coches = requete.getParameterValues("signes");
        if (coches != null) {
            Arrays.stream(coches).map(RoleSignataire::valueOf).forEach(signes::add);
        }
        return contratDao.enregistrerAvecSignatures(contrat, signes);
    }
}
