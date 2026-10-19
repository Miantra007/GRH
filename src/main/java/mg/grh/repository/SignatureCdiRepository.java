package mg.grh.repository;

import mg.grh.model.SignatureCdi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SignatureCdiRepository extends JpaRepository<SignatureCdi, Integer> {
}
