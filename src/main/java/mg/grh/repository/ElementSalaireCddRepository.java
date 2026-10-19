package mg.grh.repository;

import mg.grh.model.ElementSalaireCdd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ElementSalaireCddRepository extends JpaRepository<ElementSalaireCdd, Integer> {
}
