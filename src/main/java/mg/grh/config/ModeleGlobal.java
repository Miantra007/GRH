package mg.grh.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/** Attribut disponible dans toutes les pages : sert à surligner le lien actif du menu. */
@ControllerAdvice
public class ModeleGlobal {

    @ModelAttribute("uriCourante")
    public String uriCourante(HttpServletRequest requete) {
        return requete.getRequestURI().substring(requete.getContextPath().length());
    }
}
