package mg.grh.controller.web;

import jakarta.servlet.http.HttpSession;
import mg.grh.dto.BesoinCreationForm;
import mg.grh.model.Direction;
import mg.grh.repository.DepartementRepository;
import mg.grh.repository.DirectionRepository;
import mg.grh.repository.PosteRepository;
import mg.grh.repository.TypeBesoinRepository;
import mg.grh.service.BesoinService;
import mg.grh.web.SessionUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/** Saisie des besoins de recrutement par le directeur de la direction concernée. */
@Controller
@RequestMapping("/directeur/besoins")
public class DirecteurBesoinWebController {
    private final BesoinService besoinService;
    private final DirectionRepository directionRepository;
    private final DepartementRepository departementRepository;
    private final TypeBesoinRepository typeBesoinRepository;
    private final PosteRepository posteRepository;

    public DirecteurBesoinWebController(BesoinService besoinService, DirectionRepository directionRepository,
            DepartementRepository departementRepository, TypeBesoinRepository typeBesoinRepository,
            PosteRepository posteRepository) {
        this.besoinService = besoinService;
        this.directionRepository = directionRepository;
        this.departementRepository = departementRepository;
        this.typeBesoinRepository = typeBesoinRepository;
        this.posteRepository = posteRepository;
    }

    @GetMapping("/nouveau")
    public String nouveau(HttpSession session, Model model) {
        BesoinCreationForm formulaire = new BesoinCreationForm();
        formulaire.getPostes().add(new BesoinCreationForm.LignePoste());
        preparerModele(session, model, formulaire);
        return "directeur/besoin-form";
    }

    @PostMapping
    public String creer(@ModelAttribute("formulaire") BesoinCreationForm formulaire, HttpSession session, Model model) {
        try {
            besoinService.creerPourDirecteur(directeur(session).getId(), formulaire);
            model.addAttribute("succes", "Le besoin et ses postes ont été enregistrés et transmis au service RH.");
            BesoinCreationForm vide = new BesoinCreationForm();
            vide.getPostes().add(new BesoinCreationForm.LignePoste());
            preparerModele(session, model, vide);
        } catch (RuntimeException e) {
            model.addAttribute("erreur", e.getMessage() == null ? "Impossible d'enregistrer le besoin." : e.getMessage());
            preparerModele(session, model, formulaire);
        }
        return "directeur/besoin-form";
    }

    private void preparerModele(HttpSession session, Model model, BesoinCreationForm formulaire) {
        SessionUser directeur = directeur(session);
        List<Direction> directions = directionRepository.findAll().stream()
                .filter(direction -> direction.getDirecteur() != null && directeur.getId().equals(direction.getDirecteur().getId()))
                .toList();
        // Seuls les postes des directions dirigées par ce directeur peuvent être demandés.
        java.util.Set<Integer> idsDirections = directions.stream().map(Direction::getId)
                .collect(java.util.stream.Collectors.toSet());
        model.addAttribute("formulaire", formulaire);
        model.addAttribute("directions", directions);
        model.addAttribute("departements", departementRepository.findAll());
        model.addAttribute("typesBesoin", typeBesoinRepository.findAll());
        model.addAttribute("postes", posteRepository.findAll().stream()
                .filter(p -> p.getDirection() != null && idsDirections.contains(p.getDirection().getId()))
                .sorted(java.util.Comparator.comparing(mg.grh.model.Poste::getLibelle,
                        java.util.Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER)))
                .toList());
    }

    private SessionUser directeur(HttpSession session) {
        Object utilisateur = session.getAttribute("user");
        if (utilisateur instanceof SessionUser user && "DIRECTEUR".equals(user.getType())) {
            return user;
        }
        throw new SecurityException("Accès réservé aux directeurs.");
    }
}
