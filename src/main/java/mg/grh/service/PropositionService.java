package mg.grh.service;

// service/PropositionService.java

import mg.grh.dto.*;
import mg.grh.model.*;
import mg.grh.repository.*;
import mg.grh.exception.*;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PropositionService {

    private static final List<String> REPONSES = List.of("OUI", "NON", "AUTRE");

    private static final Map<String, String> LABELS = Map.of(
            "SALAIRE", "Salaire mensuel",
            "HORAIRE", "Horaire de travail",
            "RESPONSABLE", "Responsable hiérarchique",
            "TRAVAIL", "Travail à effectuer",
            "DATE_PRISE_POSTE", "Date de prise de poste");

    private static final Map<String, String> STATUTS = Map.of(
            "EN_ATTENTE_CANDIDAT", "En attente de votre réponse",
            "EN_ATTENTE_RH", "En attente de la décision du RH",
            "ACCORD_FINAL", "Accord final",
            "REFUSEE_CANDIDAT", "Refusée par le candidat",
            "REFUSEE_RH", "Contre-proposition refusée par le RH");

    private final PropositionEmbaucheRepository propositions;
    private final CritereRepository criteres;
    private final TypeCritereRepository types;
    private final StatutPropositionRepository statuts;
    private final ReponseCandidatRepository reponsesCandidat;
    private final ReponseCritereRepository reponsesCritere;
    private final DecisionRhRepository decisions;
    private final NotificationRepository notifications;

    // ------------------------------------------------------------------
    // Consultation
    // ------------------------------------------------------------------

    @Transactional
    public FormulaireVue vueCandidat(Integer idCandidat, Integer idProposition) {
        PropositionInfo info = info(idProposition);
        verifierProprietaire(info, idCandidat);
        return construireVue(info);
    }

    @Transactional
    public FormulaireVue vueRh(Integer idProposition) {
        return construireVue(info(idProposition));
    }

    // ------------------------------------------------------------------
    // Réponse du candidat
    // ------------------------------------------------------------------

    @Transactional
    public void soumettre(Integer idCandidat, Integer idProposition, ReponseForm form) {
        PropositionInfo info = info(idProposition);
        verifierProprietaire(info, idCandidat);
        PropositionEmbauche p = proposition(idProposition);

        if (!"EN_ATTENTE_CANDIDAT".equals(p.getStatut().getLibelle())) {
            throw new RegleMetierException("Cette proposition a déjà reçu une réponse.");
        }

        LocalDateTime maintenant = LocalDateTime.now();
        boolean autre = false, non = false;
        BigDecimal salaireDemande = null;
        List<String> resume = new ArrayList<>();

        for (CritereProposition c : assurerCriteres(p)) {
            String code = c.getType().getLibelle();
            String label = LABELS.getOrDefault(code, code);
            String rep = form.getReponse().get(code);

            if (rep == null || !REPONSES.contains(rep)) {
                throw new RegleMetierException("Veuillez répondre à : " + label);
            }

            String contre = null;
            if (rep.equals("AUTRE")) {
                contre = Optional.ofNullable(form.getContre().get(code)).orElse("").trim();
                if (contre.isEmpty()) {
                    throw new RegleMetierException("Indiquez votre proposition pour : " + label);
                }
                if (code.equals("SALAIRE")) {
                    salaireDemande = lireMontant(contre);
                }
                resume.add(label + " : " + contre);
                autre = true;
            }
            if (rep.equals("NON")) {
                non = true;
            }

            c.setReponse(reponsesCritere.findByLibelle(rep).orElseThrow());
            c.setContreProposition(contre);
            c.setDateReponse(maintenant);
        }

        String action = Optional.ofNullable(form.getAction()).orElse("");
        String nom = info.getPrenomCandidat() + " " + info.getNomCandidat();

        switch (action) {
            case "VALIDER" -> {
                if (autre || non) {
                    throw new RegleMetierException("Valider n'est possible que si tous les critères sont acceptés.");
                }
                enregistrerReponse(p, "OUI", "ACCORD_FINAL", maintenant);
                p.setSalaireFinal(salaireInitial(info));
                notifierRh(info, "Proposition acceptée",
                        nom + " a accepté la proposition d'embauche pour « " + info.getTitreOffre() + " ».");
            }
            case "REFUSER" -> {
                if (!non || autre) {
                    throw new RegleMetierException("Refuser nécessite au moins un « Non » et aucun « Autre ».");
                }
                enregistrerReponse(p, "NON", "REFUSEE_CANDIDAT", maintenant);
                notifierRh(info, "Proposition refusée",
                        nom + " a refusé la proposition d'embauche pour « " + info.getTitreOffre() + " ».");
            }
            case "ATTENDRE" -> {
                if (!autre) {
                    throw new RegleMetierException("Attendre nécessite au moins un critère « Autre ».");
                }
                enregistrerReponse(p, "AUTRE", "EN_ATTENTE_RH", maintenant);
                p.setContreProposition(String.join("\n", resume));
                p.setSalaireDemande(salaireDemande);
                notifierRh(info, "Contre-proposition à examiner",
                        nom + " a fait une contre-proposition pour « " + info.getTitreOffre() + " » : "
                                + String.join(" ; ", resume));
            }
            default -> throw new RegleMetierException("Action inconnue.");
        }
    }

    // ------------------------------------------------------------------
    // Décision du RH sur la contre-proposition
    // ------------------------------------------------------------------

    @Transactional
    public void decider(Integer idProposition, boolean accepter, String commentaire) {
        PropositionInfo info = info(idProposition);
        PropositionEmbauche p = proposition(idProposition);

        if (!"EN_ATTENTE_RH".equals(p.getStatut().getLibelle())) {
            throw new RegleMetierException("Aucune contre-proposition en attente de décision.");
        }

        String note = commentaire == null ? "" : commentaire.trim();
        p.setDecisionRh(decisions.findByLibelle(accepter ? "ACCEPTEE" : "REFUSEE").orElseThrow());
        p.setCommentaireRh(note.isEmpty() ? null : note);
        p.setDateDecisionRh(LocalDateTime.now());
        p.setStatut(statuts.findByLibelle(accepter ? "ACCORD_FINAL" : "REFUSEE_RH").orElseThrow());

        String message;
        if (accepter) {
            // salaire du candidat s'il en a proposé un, sinon celui décidé lors du besoin
            p.setSalaireFinal(p.getSalaireDemande() != null ? p.getSalaireDemande() : salaireInitial(info));
            message = "Le service RH a accepté votre contre-proposition. Vous pouvez poursuivre votre recrutement.";
        } else {
            message = "Le service RH a refusé votre contre-proposition.";
        }
        if (!note.isEmpty()) {
            message += " Message du RH : " + note;
        }
        notifierCandidat(info, accepter ? "Contre-proposition acceptée" : "Contre-proposition refusée", message);
    }

    // ------------------------------------------------------------------
    // Méthodes internes
    // ------------------------------------------------------------------

    private FormulaireVue construireVue(PropositionInfo info) {
        PropositionEmbauche p = proposition(info.getIdProposition());

        List<CritereLigne> lignes = assurerCriteres(p).stream().map(c -> {
            String code = c.getType().getLibelle();
            return new CritereLigne(code, LABELS.getOrDefault(code, code), valeurProposee(code, info),
                    c.getReponse() == null ? null : c.getReponse().getLibelle(),
                    c.getContreProposition());
        }).toList();

        String statut = p.getStatut().getLibelle();
        return new FormulaireVue(p.getId(), info.getIdCandidat(),
                info.getPrenomCandidat() + " " + info.getNomCandidat(), info.getTitreOffre(),
                STATUTS.getOrDefault(statut, statut),
                "EN_ATTENTE_CANDIDAT".equals(statut), "EN_ATTENTE_RH".equals(statut),
                p.getCommentaireRh(), lignes);
    }

    /** Valeur pré-remplie : ce que le RH a décidé lors du besoin (besoin_poste). */
    private String valeurProposee(String code, PropositionInfo info) {
        return switch (code) {
            case "SALAIRE" -> info.getSalaireInitial() == null ? ""
                    : NumberFormat.getIntegerInstance(Locale.FRANCE).format(info.getSalaireInitial()) + " Ar";
            case "HORAIRE" -> Objects.toString(info.getHoraireTravail(), "");
            case "RESPONSABLE" -> Objects.toString(info.getResponsableHierarchique(), "");
            case "TRAVAIL" -> Objects.toString(info.getMissions(), "");
            case "DATE_PRISE_POSTE" -> Objects.toString(info.getDatePrisePoste(), "");
            default -> "";
        };
    }

    private BigDecimal salaireInitial(PropositionInfo info) {
        if (info.getSalaireInitial() == null) {
            throw new RegleMetierException("Aucun salaire n'est défini pour ce poste dans le besoin.");
        }
        return info.getSalaireInitial();
    }

    /** Crée les critères manquants (au cas où la proposition n'en a pas encore). */
    private List<CritereProposition> assurerCriteres(PropositionEmbauche p) {
        List<CritereProposition> existants = new ArrayList<>(
                criteres.findByPropositionIdOrderByTypeIdAsc(p.getId()));
        Set<Integer> presents = existants.stream()
                .map(c -> c.getType().getId()).collect(Collectors.toSet());

        boolean ajoute = false;
        for (TypeCritereProposition t : types.findAll(Sort.by("id"))) {
            if (!presents.contains(t.getId())) {
                CritereProposition c = new CritereProposition();
                c.setProposition(p);
                c.setType(t);
                existants.add(criteres.save(c));
                ajoute = true;
            }
        }
        if (ajoute) {
            existants.sort(Comparator.comparing(c -> c.getType().getId()));
        }
        return existants;
    }

    private void enregistrerReponse(PropositionEmbauche p, String reponse, String statut, LocalDateTime date) {
        p.setReponseCandidat(reponsesCandidat.findByLibelle(reponse).orElseThrow());
        p.setDateReponseCandidat(date);
        p.setStatut(statuts.findByLibelle(statut).orElseThrow());
    }

    private BigDecimal lireMontant(String texte) {
        try {
            BigDecimal m = new BigDecimal(texte.replaceAll("\\s", ""));
            if (m.signum() <= 0) {
                throw new NumberFormatException();
            }
            return m.setScale(0, RoundingMode.HALF_UP);
        } catch (NumberFormatException e) {
            throw new RegleMetierException("Le salaire demandé doit être un nombre positif.");
        }
    }

    private void notifierRh(PropositionInfo info, String titre, String message) {
        if (info.getIdRhTraitant() == null) {
            return;
        }
        notifications.save(Notification.pour(null, info.getIdRhTraitant(), info.getIdProposition(), titre, message));
    }

    private void notifierCandidat(PropositionInfo info, String titre, String message) {
        notifications.save(Notification.pour(info.getIdCandidat(), null, info.getIdProposition(), titre, message));
    }

    private PropositionInfo info(Integer idProposition) {
        return propositions.findInfo(idProposition)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Proposition introuvable."));
    }

    private PropositionEmbauche proposition(Integer idProposition) {
        return propositions.findById(idProposition)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Proposition introuvable."));
    }

    private void verifierProprietaire(PropositionInfo info, Integer idCandidat) {
        if (!info.getIdCandidat().equals(idCandidat)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Proposition introuvable.");
        }
    }
}