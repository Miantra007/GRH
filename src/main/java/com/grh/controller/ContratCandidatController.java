package com.grh.controller;

import com.grh.controller.crud.Champ;
import com.grh.controller.crud.CrudController;
import com.grh.controller.crud.Ecran;
import com.grh.controller.crud.Groupe;
import com.grh.dao.ContratCandidatDao;
import com.grh.model.Candidat;
import com.grh.model.ContratCandidat;
import com.grh.model.ContratCdd;
import com.grh.model.ContratCdi;
import com.grh.model.ContratCe;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Controller
@RequestMapping("/contrats/candidats")
public class ContratCandidatController extends CrudController<ContratCandidat> {

    public ContratCandidatController(ContratCandidatDao dao) {
        super(dao, ContratCandidat::new,
                new Ecran("/contrats/candidats", "Contrats des candidats", "contrat de candidat", "fa-user-check", Groupe.CONTRATS, 8),
                List.of(
                        Champ.relation("candidat", "Candidat", Candidat.class).enListe(),
                        Champ.relation("contratCe", "Contrat d'essai", ContratCe.class).enListe(),
                        Champ.relation("contratCdd", "Contrat CDD", ContratCdd.class).enListe(),
                        Champ.relation("contratCdi", "Contrat CDI", ContratCdi.class).enListe()
                ));
    }

    @Override
    protected void valider(ContratCandidat lien, BindingResult resultat) {
        long renseignes = Stream.of(lien.getContratCe(), lien.getContratCdd(), lien.getContratCdi())
                .filter(Objects::nonNull).count();
        if (renseignes != 1) {
            resultat.reject("unContrat", "Choisissez exactement un contrat : d'essai, CDD ou CDI.");
        }
    }
}
