package mg.grh.repository;

import mg.grh.model.PosteFille;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PosteFilleRepository extends JpaRepository<PosteFille, Integer> {
}
