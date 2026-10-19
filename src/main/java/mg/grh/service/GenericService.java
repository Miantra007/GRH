package mg.grh.service;

import mg.grh.exception.ResourceNotFoundException;
import mg.grh.model.Identifiable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public abstract class GenericService<T extends Identifiable> {

    protected final JpaRepository<T, Integer> repository;

    protected GenericService(JpaRepository<T, Integer> repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<T> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public T findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ressource introuvable : id=" + id));
    }

    public T create(T entity) {
        entity.setId(null);
        return repository.save(entity);
    }

    public T update(Integer id, T entity) {
        findById(id);
        entity.setId(id);
        return repository.save(entity);
    }

    public void delete(Integer id) {
        findById(id);
        repository.deleteById(id);
    }
}
