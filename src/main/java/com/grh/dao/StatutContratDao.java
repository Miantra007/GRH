package com.grh.dao;

import com.grh.model.StatutContrat;
import com.grh.repository.StatutContratRepository;
import org.springframework.stereotype.Repository;

@Repository
public class StatutContratDao extends GenericDao<StatutContrat> {

    public static final String STATUT_INITIAL = "Créé";

    private final StatutContratRepository statutRepository;

    public StatutContratDao(StatutContratRepository repository) {
        super(repository, "StatutContrat");
        this.statutRepository = repository;
    }

    /** Statut proposé à la création d'un contrat (null s'il a été supprimé du référentiel). */
    public StatutContrat statutInitial() {
        return statutRepository.findByLibelle(STATUT_INITIAL).orElse(null);
    }
}
