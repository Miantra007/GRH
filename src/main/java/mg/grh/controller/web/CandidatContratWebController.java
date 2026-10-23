package mg.grh.controller.web;

import jakarta.servlet.http.HttpSession;
import mg.grh.model.ContratCandidat;
import mg.grh.service.ContratCandidatService;
import mg.grh.service.*;
import mg.grh.model.*;
import mg.grh.repository.PropositionEmbaucheRepository;
import mg.grh.web.SessionUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

/** Côté Candidat : consulte ses contrats et les signe (statut "Signé"). */
@Controller
@RequestMapping("/candidat")
public class CandidatContratWebController {

    private final ContratCandidatService contratService;
    private final PropositionEmbaucheRepository propositions;
    private final CandidatService candidatService;
    private final EmployeurService employeurService;
    private final TravailleurService travailleurService;
    private final LangueContratService langueService;
    private final CategorieProfessionnelleService categorieService;
    private final ConventionCollectiveService conventionService;
    private final PosteService posteService;
    private final MotifCddService motifService;
    private final NationaliteService nationaliteService;
    private final CiviliteService civiliteService;
    private final SituationFamilleService situationFamilleService;

    public CandidatContratWebController(ContratCandidatService contratService, PropositionEmbaucheRepository propositions,
            CandidatService candidatService, EmployeurService employeurService, TravailleurService travailleurService,
            LangueContratService langueService, CategorieProfessionnelleService categorieService,
            ConventionCollectiveService conventionService, PosteService posteService, MotifCddService motifService,
            NationaliteService nationaliteService, CiviliteService civiliteService,
            SituationFamilleService situationFamilleService) {
        this.contratService = contratService;
        this.propositions = propositions; this.candidatService = candidatService; this.employeurService = employeurService;
        this.travailleurService = travailleurService; this.langueService = langueService; this.categorieService = categorieService;
        this.conventionService = conventionService; this.posteService = posteService; this.motifService = motifService;
        this.nationaliteService = nationaliteService; this.civiliteService = civiliteService;
        this.situationFamilleService = situationFamilleService;
    }

    @GetMapping("/contrats/nouveau")
    public String nouveau(@RequestParam Integer propositionId, HttpSession session, Model model) {
        SessionUser user = user(session);
        PropositionInfo info = propositions.findInfo(propositionId).orElseThrow();
        if (!user.getId().equals(info.getIdCandidat())) throw new SecurityException("Cette proposition ne vous appartient pas.");
        PropositionEmbauche proposition = propositions.findAccordFinal(propositionId).orElseThrow();
        model.addAttribute("candidat", candidatService.findById(user.getId()));
        model.addAttribute("proposition", proposition);
        model.addAttribute("propositionInfo", info);
        model.addAttribute("propositionId", propositionId); model.addAttribute("type", "CE");
        model.addAttribute("candidatures", java.util.List.of());
        model.addAttribute("employeurs", employeurService.findAll());
        model.addAttribute("travailleurs", travailleurService.findAll().stream().filter(t -> t.getEmail() != null && t.getEmail().equalsIgnoreCase(user.getEmail())).toList());
        model.addAttribute("langues", langueService.findAll()); model.addAttribute("categories", categorieService.findAll());
        model.addAttribute("conventions", conventionService.findAll()); model.addAttribute("postes", posteService.findAll());
        model.addAttribute("motifs", motifService.findAll()); model.addAttribute("salaire", proposition.getSalaireFinal());
        model.addAttribute("nationalites", nationaliteService.findAll()); model.addAttribute("civilites", civiliteService.findAll());
        model.addAttribute("situationsFamiliales", situationFamilleService.findAll());
        model.addAttribute("datePrisePoste", info.getDatePrisePoste()); model.addAttribute("missions", info.getMissions());
        model.addAttribute("datePrisePosteInput", java.time.LocalDate.parse(info.getDatePrisePoste(), java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        model.addAttribute("horaire", info.getHoraireTravail());
        return "admin/contrat-form";
    }

    @PostMapping("/contrats/ce")
    public String creerCe(@RequestParam Integer propositionId, @ModelAttribute ContratCe contrat, HttpSession session, RedirectAttributes ra) {
        try { return apresCreation(contratService.creerCe(user(session).getId(), propositionId, contrat), ra); }
        catch (RuntimeException e) { ra.addFlashAttribute("erreur", e.getMessage()); return "redirect:/candidat/contrats/nouveau?propositionId=" + propositionId; }
    }

    @PostMapping("/contrats/cdd")
    public String creerCdd(@RequestParam Integer propositionId, @ModelAttribute ContratCdd contrat, HttpSession session, RedirectAttributes ra) {
        try { return apresCreation(contratService.creerCdd(user(session).getId(), propositionId, contrat), ra); }
        catch (RuntimeException e) { ra.addFlashAttribute("erreur", e.getMessage()); return "redirect:/candidat/contrats/nouveau?propositionId=" + propositionId; }
    }

    @PostMapping("/contrats/cdi")
    public String creerCdi(@RequestParam Integer propositionId, @ModelAttribute ContratCdi contrat, HttpSession session, RedirectAttributes ra) {
        try { return apresCreation(contratService.creerCdi(user(session).getId(), propositionId, contrat), ra); }
        catch (RuntimeException e) { ra.addFlashAttribute("erreur", e.getMessage()); return "redirect:/candidat/contrats/nouveau?propositionId=" + propositionId; }
    }

    private String apresCreation(ContratCandidat cc, RedirectAttributes ra) {
        ra.addFlashAttribute("succes", "Contrat créé. Vous pouvez le signer depuis Mes contrats.");
        return "redirect:/candidat/contrats/" + cc.getId();
    }

    @GetMapping("/contrats")
    public String mesContrats(HttpSession session, Model model) {
        model.addAttribute("contrats", contratService.findByCandidat(user(session).getId()));
        return "candidat/contrats";
    }

    @GetMapping("/contrats/{id}")
    public String detail(@PathVariable Integer id, HttpSession session, Model model, RedirectAttributes ra) {
        ContratCandidat cc = contratService.findById(id);
        if (!cc.getCandidat().getId().equals(user(session).getId())) {
            ra.addFlashAttribute("erreur", "Ce contrat ne vous appartient pas.");
            return "redirect:/candidat/contrats";
        }
        model.addAttribute("cc", cc);
        model.addAttribute("historique", contratService.historique(id));
        return "contrat-detail";
    }

    @PostMapping("/contrats/{id}/signer")
    public String signer(@PathVariable Integer id, HttpSession session, RedirectAttributes ra) {
        try {
            contratService.signer(id, user(session).getId());
            ra.addFlashAttribute("succes", "Contrat signé. Il est maintenant en attente de validation par le RH.");
        } catch (RuntimeException e) {
            ra.addFlashAttribute("erreur", e.getMessage());
        }
        return "redirect:/candidat/contrats/" + id;
    }

    private SessionUser user(HttpSession session) {
        return (SessionUser) session.getAttribute("user");
    }
}
