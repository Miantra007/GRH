package mg.grh.controller;

import mg.grh.model.SignatureCdi;
import mg.grh.service.SignatureCdiService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/signature-cdi")
public class SignatureCdiController extends GenericController<SignatureCdi> {
    public SignatureCdiController(SignatureCdiService service) {
        super(service);
    }
}
