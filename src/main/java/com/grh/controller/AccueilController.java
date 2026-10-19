package com.grh.controller;

import com.grh.config.Navigation;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccueilController {

    private final Navigation navigation;

    public AccueilController(Navigation navigation) {
        this.navigation = navigation;
    }

    @GetMapping("/")
    public String accueil(Model model) {
        model.addAttribute("rubriques", navigation.getRubriques());
        return "accueil";
    }
}
