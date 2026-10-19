package com.grh.dao;

import com.grh.model.HistoriqueStatutContrat;
import com.grh.repository.HistoriqueStatutContratRepository;
import org.springframework.stereotype.Repository;

@Repository
public class HistoriqueStatutContratDao extends GenericDao<HistoriqueStatutContrat> {

    public HistoriqueStatutContratDao(HistoriqueStatutContratRepository repository) {
        super(repository, "HistoriqueStatutContrat");
    }
}
