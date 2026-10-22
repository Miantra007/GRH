package mg.grh.controller;

import mg.grh.model.*;
import mg.grh.service.ContratCandidatService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contrats-candidat")
public class ContratCandidatController {

    private final ContratCandidatService service;

    public ContratCandidatController(ContratCandidatService service) {
        this.service = service;
    }

    // ---- consultation
    @GetMapping
    public List<ContratCandidat> findAll(@RequestParam(required = false) Integer candidatId) {
        return candidatId == null ? service.findAll() : service.findByCandidat(candidatId);
    }

    @GetMapping("/a-valider")
    public List<ContratCandidat> aValider() {
        return service.findAValider();
    }

    @GetMapping("/{id}")
    public ContratCandidat findById(@PathVariable Integer id) {
        return service.findById(id);
    }

    @GetMapping("/{id}/historique")
    public List<HistoriqueStatutContrat> historique(@PathVariable Integer id) {
        return service.historique(id);
    }

    // ---- 1) RH : création (statut Créé). CE = choix par défaut du formulaire
    @PostMapping("/ce")
    @ResponseStatus(HttpStatus.CREATED)
    public ContratCandidat creerCe(@RequestParam Integer candidatId, @RequestParam(required = false) Integer candidatureId,
                                   @RequestBody ContratCe contrat) {
        return service.creerCe(candidatId, contrat, candidatureId);
    }

    @PostMapping("/cdd")
    @ResponseStatus(HttpStatus.CREATED)
    public ContratCandidat creerCdd(@RequestParam Integer candidatId, @RequestParam(required = false) Integer candidatureId,
                                   @RequestBody ContratCdd contrat) {
        return service.creerCdd(candidatId, contrat, candidatureId);
    }

    @PostMapping("/cdi")
    @ResponseStatus(HttpStatus.CREATED)
    public ContratCandidat creerCdi(@RequestParam Integer candidatId, @RequestParam(required = false) Integer candidatureId,
                                   @RequestBody ContratCdi contrat) {
        return service.creerCdi(candidatId, contrat, candidatureId);
    }

    // ---- 2) Candidat : signature (statut Signé)
}
