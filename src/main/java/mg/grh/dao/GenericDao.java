package mg.grh.dao;

import mg.grh.model.Identifiable;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/** DAO générique basé sur l'EntityManager (JPQL pour les requêtes personnalisées). */
public abstract class GenericDao<T extends Identifiable> {

    @PersistenceContext
    protected EntityManager em;

    private final Class<T> type;

    protected GenericDao(Class<T> type) {
        this.type = type;
    }

    public List<T> findAll() {
        return em.createQuery("select e from " + type.getSimpleName() + " e", type).getResultList();
    }

    public Optional<T> findById(Integer id) {
        return Optional.ofNullable(em.find(type, id));
    }

    @Transactional
    public T save(T entity) {
        if (entity.getId() == null) {
            em.persist(entity);
            return entity;
        }
        return em.merge(entity);
    }

    @Transactional
    public void deleteById(Integer id) {
        T e = em.find(type, id);
        if (e != null) em.remove(e);
    }

    public long count() {
        return em.createQuery("select count(e) from " + type.getSimpleName() + " e", Long.class).getSingleResult();
    }
}
