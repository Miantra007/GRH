package mg.grh.controller;

import mg.grh.model.Role;
import mg.grh.service.RoleService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/role")
public class RoleController extends GenericController<Role> {
    public RoleController(RoleService service) {
        super(service);
    }
}
