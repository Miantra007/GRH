package mg.grh.controller.web;

import mg.grh.model.*;
import mg.grh.web.SessionUser;
import jakarta.servlet.http.HttpSession;
import mg.grh.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

/** Côté RH (Administrateur RH) : création des contrats, statut initial "Créé". */
@Controller
@RequestMapping("/admin")
public class AdminContratWebController {

    private final ContratCandidatService contratService;
    private final CandidatService candidatService;
    private final EmployeurService employeurService;
    private final TravailleurService travailleurService;
    private final LangueContratService langueService;
    private final CategorieProfessionnelleService categorieService;
    private final ConventionCollectiveService conventionService;
    private final PosteService posteService;
    private final MotifCddService motifService;
    private final StatutContratService statutService;
    private final CiviliteService civiliteService;
    private final NationaliteService nationaliteService;
    private final SituationFamilleService situationService;
    private final EmbaucheService embaucheService;
    private final SexeService sexeService;

    public AdminContratWebController(ContratCandidatService contratService, CandidatService candidatService,
                                     EmployeurService employeurService, TravailleurService travailleurService,
                                     LangueContratService langueService, CategorieProfessionnelleService categorieService,
                                     ConventionCollectiveService conventionService, PosteService posteService,
                                     MotifCddService motifService, StatutContratService statutService,
                                     CiviliteService civiliteService, NationaliteService nationaliteService,
                                     SituationFamilleService situationService, EmbaucheService embaucheService,
                                     SexeService sexeService) {
        this.contratService = contratService;
        this.candidatService = candidatService;
        this.employeurService = employeurService;
        this.travailleurService = travailleurService;
        this.langueService = langueService;
        this.categorieService = categorieService;
        this.conventionService = conventionService;
        this.posteService = posteService;
        this.motifService = motifService;
        this.statutService = statutService;
        this.civiliteService = civiliteService;
        this.nationaliteService = nationaliteService;
        this.situationService = situationService;
        this.embaucheService = embaucheService;
        this.sexeService = sexeService;
    }

    // ------------------------------------------------------------ candidats
    @GetMapping("/candidats")
    public String candidats(Model model) {
        model.addAttribute("candidats", candidatService.findAll());
        return "admin/candidats";
    }

    // ------------------------------------------------------------ liste + détail
    @GetMapping("/contrats")
    public String liste(@RequestParam(required = false) String statut, Model model) {
        List<ContratCandidat> contrats = contratService.findAll().stream()
                .filter(cc -> statut == null || statut.isBlank()
                        || (cc.getStatutActuel() != null && statut.equals(cc.getStatutActuel().getLibelle())))
                .toList();
        model.addAttribute("contrats", contrats);
        model.addAttribute("statuts", statutService.findAll());
        model.addAttribute("statutFiltre", statut);
        return "admin/contrats";
    }

    @GetMapping("/contrats/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        ContratCandidat cc = contratService.findById(id);
        model.addAttribute("cc", cc);
        model.addAttribute("historique", contratService.historique(id));
        model.addAttribute("besoin", embaucheService.apercu(cc));
        model.addAttribute("embauche", embaucheService.embauchePour(id).orElse(null));
        return "contrat-detail";
    }

    @PostMapping("/contrats/{id}/valider")
    public String valider(@PathVariable Integer id, HttpSession session, RedirectAttributes ra) {
        try {
            SessionUser rh = (SessionUser) session.getAttribute("user");
            mg.grh.dto.ResultatValidation resultat = contratService.valider(id, rh.getId());
            ra.addFlashAttribute("succes", "Contrat validé par le RH. Le candidat a été notifié. "
                    + resultat.embauche().message());
        } catch (RuntimeException e) {
            ra.addFlashAttribute("erreur", e.getMessage());
        }
        return "redirect:/admin/contrats/" + id;
    }

    // ------------------------------------------------------------ formulaire de création (CE par défaut)
    @GetMapping("/contrats/nouveau")
    public String nouveau(@RequestParam Integer candidatId, @RequestParam(defaultValue = "CE") String type, Model model) {
        model.addAttribute("candidat", candidatService.findById(candidatId));
        model.addAttribute("type", type);
        model.addAttribute("employeurs", employeurService.findAll());
        model.addAttribute("travailleurs", travailleurService.findAll());
        model.addAttribute("langues", langueService.findAll());
        model.addAttribute("categories", categorieService.findAll());
        model.addAttribute("conventions", conventionService.findAll());
        model.addAttribute("postes", posteService.findAll());
        model.addAttribute("motifs", motifService.findAll());
        model.addAttribute("candidatures", contratService.candidaturesRetenues(candidatId));
        model.addAttribute("nationalites", nationaliteService.findAll());
        model.addAttribute("civilites", civiliteService.findAll());
        model.addAttribute("situationsFamiliales", situationService.findAll());
        model.addAttribute("sexes", sexeService.findAll());
        return "admin/contrat-form";
    }

    @PostMapping("/contrats/ce")
    public String creerCe(@RequestParam Integer candidatId, @RequestParam(required = false) Integer candidatureId,
                           @ModelAttribute ContratCe ce,
                          BindingResult br, RedirectAttributes ra) {
        if (br.hasErrors()) return erreur(ra, candidatId, "CE", "Champ invalide : " + br.getFieldError().getField());
        if (ce.getConvention() != null && ce.getConvention().getId() == null) ce.setConvention(null);
        ce.setContratInitial(null);
        try {
            ContratCandidat cc = contratService.creerCe(candidatId, ce, candidatureId);
            return succes(ra, cc);
        } catch (RuntimeException e) {
            return erreur(ra, candidatId, "CE", e.getMessage());
        }
    }

    @PostMapping("/contrats/cdd")
    public String creerCdd(@RequestParam Integer candidatId, @RequestParam(required = false) Integer candidatureId,
                           @ModelAttribute ContratCdd cdd,
                           BindingResult br, RedirectAttributes ra) {
        if (br.hasErrors()) return erreur(ra, candidatId, "CDD", "Champ invalide : " + br.getFieldError().getField());
        if (cdd.getCategorie() != null && cdd.getCategorie().getId() == null) cdd.setCategorie(null);
        if (cdd.getConvention() != null && cdd.getConvention().getId() == null) cdd.setConvention(null);
        try {
            return succes(ra, contratService.creerCdd(candidatId, cdd, candidatureId));
        } catch (RuntimeException e) {
            return erreur(ra, candidatId, "CDD", e.getMessage());
        }
    }

    @PostMapping("/contrats/cdi")
    public String creerCdi(@RequestParam Integer candidatId, @RequestParam(required = false) Integer candidatureId,
                           @ModelAttribute ContratCdi cdi,
                           BindingResult br, RedirectAttributes ra) {
        if (br.hasErrors()) return erreur(ra, candidatId, "CDI", "Champ invalide : " + br.getFieldError().getField());
        if (cdi.getCategorie() != null && cdi.getCategorie().getId() == null) cdi.setCategorie(null);
        if (cdi.getConvention() != null && cdi.getConvention().getId() == null) cdi.setConvention(null);
        try {
            return succes(ra, contratService.creerCdi(candidatId, cdi, candidatureId));
        } catch (RuntimeException e) {
            return erreur(ra, candidatId, "CDI", e.getMessage());
        }
    }

    // ------------------------------------------------------------ travailleur (partie du contrat)
    @GetMapping("/travailleurs/nouveau")
    public String nouveauTravailleur(@RequestParam Integer candidatId, Model model) {
        model.addAttribute("candidatId", candidatId);
        model.addAttribute("civilites", civiliteService.findAll());
        model.addAttribute("nationalites", nationaliteService.findAll());
        model.addAttribute("situations", situationService.findAll());
        return "admin/travailleur-form";
    }

    @PostMapping("/travailleurs")
    public String creerTravailleur(@RequestParam Integer candidatId, @ModelAttribute Travailleur t,
                                   BindingResult br, RedirectAttributes ra) {
        if (br.hasErrors()) {
            ra.addFlashAttribute("erreur", "Champ invalide : " + br.getFieldError().getField());
            return "redirect:/admin/travailleurs/nouveau?candidatId=" + candidatId;
        }
        if (t.getCivilite() != null && t.getCivilite().getId() == null) t.setCivilite(null);
        if (t.getSituationFamille() != null && t.getSituationFamille().getId() == null) t.setSituationFamille(null);
        try {
            travailleurService.create(t);
            ra.addFlashAttribute("succes", "Travailleur créé. Sélectionnez-le dans le contrat.");
            return "redirect:/admin/contrats/nouveau?candidatId=" + candidatId;
        } catch (RuntimeException e) {
            ra.addFlashAttribute("erreur", "Création impossible : " + e.getMessage());
            return "redirect:/admin/travailleurs/nouveau?candidatId=" + candidatId;
        }
    }

    // ------------------------------------------------------------ utilitaires
    private String succes(RedirectAttributes ra, ContratCandidat cc) {
        ra.addFlashAttribute("succes", "Contrat créé avec le statut « Créé ». Il attend la signature du candidat.");
        return "redirect:/admin/contrats/" + cc.getId();
    }

    private String erreur(RedirectAttributes ra, Integer candidatId, String type, String message) {
        ra.addFlashAttribute("erreur", "Création impossible : " + message);
        return "redirect:/admin/contrats/nouveau?candidatId=" + candidatId + "&type=" + type;
    }
}
