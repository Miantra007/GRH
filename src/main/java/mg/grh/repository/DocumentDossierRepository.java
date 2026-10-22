package mg.grh.repository;

import mg.grh.model.DocumentDossier;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DocumentDossierRepository extends JpaRepository<DocumentDossier, Integer> {
    List<DocumentDossier> findByDossierId(Integer dossierId);
}
