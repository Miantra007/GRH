package com.grh.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/contrat")
public class ContratController {

    @GetMapping("/cdi")
    public String formulaireCdi() {
        return "contrat/contrat-cdi";
    }
}
