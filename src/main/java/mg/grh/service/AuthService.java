package mg.grh.service;

import mg.grh.model.Candidat;
import mg.grh.model.Responsable;
import mg.grh.repository.CandidatRepository;
import mg.grh.repository.ResponsableRepository;
import mg.grh.web.SessionUser;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AuthService {

    private final ResponsableRepository responsableRepo;
    private final CandidatRepository candidatRepo;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public AuthService(ResponsableRepository responsableRepo, CandidatRepository candidatRepo) {
        this.responsableRepo = responsableRepo;
        this.candidatRepo = candidatRepo;
    }

    @Transactional(readOnly = true)
    public Optional<SessionUser> authentifier(String email, String motDePasse) {
        if (email == null || motDePasse == null) return Optional.empty();
        String mail = email.trim().toLowerCase();

        Optional<Responsable> r = responsableRepo.findByEmail(mail);
        if (r.isPresent() && matches(motDePasse, r.get().getMotDePasse())) {
            String role = r.get().getRole() == null ? "" : r.get().getRole().getLibelle();
            String type = switch (role) {
                case "Administrateur RH" -> "RH";
                case "Direction générale" -> "DG";
                case "Directeur" -> "DIRECTEUR";
                case "Financier" -> "FINANCE";
                default -> "AUTRE";
            };
            return Optional.of(new SessionUser(type, r.get().getId(), r.get().getEmail()));
        }
        Optional<Candidat> c = candidatRepo.findByEmail(mail);
        if (c.isPresent() && matches(motDePasse, c.get().getMotDePasse())) {
            return Optional.of(new SessionUser("CANDIDAT", c.get().getId(), c.get().getEmail()));
        }
        return Optional.empty();
    }

    private boolean matches(String brut, String hash) {
        try {
            return hash != null && encoder.matches(brut, hash);
        } catch (Exception e) {
            return false;   // hash invalide (ex : "hash_factice_1")
        }
    }
}
