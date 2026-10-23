package mg.grh.config;

import mg.grh.web.AuthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthInterceptor()).addPathPatterns(
                "/admin/**", "/dg/**", "/directeur/**", "/finance/**",
                "/candidat/**", "/candidats/**", "/rh/**", "/api/**")
                // Parcours public candidat : consultation des offres et dépôt de candidature
                // sans authentification (l'inscription se fait via le formulaire saisie-candidat).
                .excludePathPatterns(
                        "/api/candidat/offres", "/api/candidat/offres/*",
                        "/api/candidat/offres/*/postuler",
                        "/api/candidat/etape/**",
                        "/api/candidat/confirmation/*",
                        "/api/candidat/suivi", "/api/candidat/suivi/**");
    }
}
