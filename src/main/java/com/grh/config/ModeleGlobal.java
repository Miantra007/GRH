package com.grh.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/** Attributs disponibles dans toutes les pages. */
@ControllerAdvice
public class ModeleGlobal {

    /** Sert à surligner l'entrée de menu active. */
    @ModelAttribute("uriCourante")
    public String uriCourante(HttpServletRequest requete) {
        return requete.getRequestURI().substring(requete.getContextPath().length());
    }
}
