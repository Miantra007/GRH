package com.grh.controller;

import com.grh.controller.crud.Champ;
import com.grh.controller.crud.CrudController;
import com.grh.controller.crud.Ecran;
import com.grh.controller.crud.Groupe;
import com.grh.dao.SignatureCdiDao;
import com.grh.model.ContratCdi;
import com.grh.model.SignatureCdi;
import com.grh.model.enums.RoleSignataire;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/contrats/cdi/signatures")
public class SignatureCdiController extends CrudController<SignatureCdi> {

    public SignatureCdiController(SignatureCdiDao dao) {
        super(dao, SignatureCdi::new,
                new Ecran("/contrats/cdi/signatures", "Signatures CDI", "signature", "fa-signature", Groupe.CONTRATS, 2),
                List.of(
                        Champ.relation("contrat", "Contrat CDI", ContratCdi.class).obligatoire().enListe(),
                        Champ.enumeration("role", "Signataire", RoleSignataire.class).obligatoire().enListe(),
                        Champ.booleen("signe", "Signé").enListe()
                ));
    }
}
