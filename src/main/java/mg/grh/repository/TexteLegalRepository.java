package mg.grh.repository;

import mg.grh.model.TexteLegal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TexteLegalRepository extends JpaRepository<TexteLegal, Integer> {
}
