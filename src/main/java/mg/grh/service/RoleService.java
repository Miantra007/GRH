package mg.grh.service;

import mg.grh.model.Role;
import mg.grh.repository.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleService extends GenericService<Role> {
    public RoleService(RoleRepository repository) {
        super(repository);
    }
}
