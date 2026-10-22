package mg.grh.repository;

import mg.grh.model.PosteFille;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PosteFilleRepository extends JpaRepository<PosteFille, Integer> {

    /** Sièges d'un poste dans un département. */
    List<PosteFille> findByPosteIdAndDeptIdOrderById(Integer posteId, Integer deptId);
}
