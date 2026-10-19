package mg.grh.controller;

import mg.grh.model.Direction;
import mg.grh.service.DirectionService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/direction")
public class DirectionController extends GenericController<Direction> {
    public DirectionController(DirectionService service) {
        super(service);
    }
}
