package mg.grh.controller.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import mg.grh.service.AuthService;
import mg.grh.web.SessionUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class AuthWebController {

    private final AuthService authService;

    public AuthWebController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/")
    public String accueil(HttpSession session) {
        SessionUser u = (SessionUser) session.getAttribute("user");
        return u == null ? "redirect:/login" : "redirect:" + u.homeUrl();
    }

    @GetMapping("/login")
    public String formulaire(@RequestParam(required = false) String refuse, org.springframework.ui.Model model) {
        if (refuse != null) model.addAttribute("erreur", "Accès refusé pour ce profil. Connectez-vous avec le bon compte.");
        return "login";
    }

    @PostMapping("/login")
    public String connecter(@RequestParam String email, @RequestParam String motDePasse,
                            HttpServletRequest request, RedirectAttributes ra) {
        Optional<SessionUser> user = authService.authentifier(email, motDePasse);
        if (user.isEmpty()) {
            ra.addFlashAttribute("erreur", "Email ou mot de passe incorrect.");
            return "redirect:/login";
        }
        if ("AUTRE".equals(user.get().getType())) {
            ra.addFlashAttribute("erreur", "Ce rôle n'a pas accès au module Contrat.");
            return "redirect:/login";
        }
        HttpSession session = request.getSession(true);
        request.changeSessionId();
        session.setAttribute("user", user.get());
        return "redirect:" + user.get().homeUrl();
    }

    @PostMapping("/logout")
    public String deconnecter(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
