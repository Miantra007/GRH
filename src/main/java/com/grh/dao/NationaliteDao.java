package com.grh.dao;

import com.grh.model.Nationalite;
import com.grh.repository.NationaliteRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class NationaliteDao extends GenericDao<Nationalite> {

    private final NationaliteRepository nationaliteRepository;

    public NationaliteDao(NationaliteRepository repository) {
        super(repository, "Nationalite");
        this.nationaliteRepository = repository;
    }

    /** Nationalité choisie dans la liste /data/pays.json : réutilisée si elle existe déjà en base. */
    @Transactional
    public Nationalite trouverOuCreer(String libelle) {
        return nationaliteRepository.findByLibelleIgnoreCase(libelle).orElseGet(() -> {
            Nationalite n = new Nationalite();
            n.setLibelle(libelle);
            return nationaliteRepository.save(n);
        });
    }
}
