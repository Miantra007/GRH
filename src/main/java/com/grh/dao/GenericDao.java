package com.grh.dao;

import com.grh.exception.EntiteIntrouvableException;
import com.grh.model.Entite;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Accès aux données commun à toutes les entités.
 * Chaque DAO concret l'étend et y ajoute ses requêtes métier.
 */
public abstract class GenericDao<T extends Entite> {

    protected final JpaRepository<T, Integer> repository;
    private final String nomEntite;

    protected GenericDao(JpaRepository<T, Integer> repository, String nomEntite) {
        this.repository = repository;
        this.nomEntite = nomEntite;
    }

    public List<T> listerTous() {
        return repository.findAll(Sort.by("id"));
    }

    public Optional<T> trouver(Integer id) {
        return repository.findById(id);
    }

    public T obtenir(Integer id) {
        return repository.findById(id).orElseThrow(() -> new EntiteIntrouvableException(nomEntite, id));
    }

    public T enregistrer(T entite) {
        return repository.save(entite);
    }

    public void supprimer(Integer id) {
        repository.deleteById(id);
        repository.flush();
    }

    public long compter() {
        return repository.count();
    }
}
