package com.grh.dao;

import com.grh.model.ContratCdi;
import com.grh.model.SignatureCdi;
import com.grh.model.enums.RoleSignataire;
import com.grh.repository.ContratCdiRepository;
import com.grh.repository.SignatureCdiRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class ContratCdiDao extends GenericDao<ContratCdi> {

    private final SignatureCdiRepository signatureRepository;

    public ContratCdiDao(ContratCdiRepository repository, SignatureCdiRepository signatureRepository) {
        super(repository, "ContratCdi");
        this.signatureRepository = signatureRepository;
    }

    /** Enregistre le contrat et ses 4 lignes de signature (une par signataire). */
    @Transactional
    public ContratCdi enregistrerAvecSignatures(ContratCdi contrat, Set<RoleSignataire> signes) {
        ContratCdi enregistre = repository.save(contrat);

        Map<RoleSignataire, SignatureCdi> existantes = new EnumMap<>(RoleSignataire.class);
        signatureRepository.findByContrat(enregistre).forEach(s -> existantes.put(s.getRole(), s));

        for (RoleSignataire role : RoleSignataire.values()) {
            SignatureCdi s = existantes.computeIfAbsent(role, r -> {
                SignatureCdi nouvelle = new SignatureCdi();
                nouvelle.setContrat(enregistre);
                nouvelle.setRole(r);
                return nouvelle;
            });
            s.setSigne(signes.contains(role));
            signatureRepository.save(s);
        }
        return enregistre;
    }

    public Set<RoleSignataire> signataires(ContratCdi contrat) {
        if (contrat.getId() == null) {
            return EnumSet.noneOf(RoleSignataire.class);
        }
        return signatureRepository.findByContrat(contrat).stream()
                .filter(s -> Boolean.TRUE.equals(s.getSigne()))
                .map(SignatureCdi::getRole)
                .collect(Collectors.toCollection(() -> EnumSet.noneOf(RoleSignataire.class)));
    }
}
