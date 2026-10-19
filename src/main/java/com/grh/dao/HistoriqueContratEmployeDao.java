package com.grh.dao;

import com.grh.model.HistoriqueContratEmploye;
import com.grh.repository.HistoriqueContratEmployeRepository;
import org.springframework.stereotype.Repository;

@Repository
public class HistoriqueContratEmployeDao extends GenericDao<HistoriqueContratEmploye> {

    public HistoriqueContratEmployeDao(HistoriqueContratEmployeRepository repository) {
        super(repository, "HistoriqueContratEmploye");
    }
}
