package mg.grh.controller.web;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

import java.beans.PropertyEditorSupport;

/** Les champs texte vides des formulaires deviennent null (sinon "" violerait les CHECK de la base).
 *  On ne fait volontairement aucun trim() pour ne pas altérer les mots de passe. */
@ControllerAdvice(basePackages = "mg.grh.controller.web")
public class WebBindingAdvice {
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(text == null || text.isEmpty() ? null : text);
            }
        });
    }
}
