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
                "/candidat/**", "/candidats/**", "/rh/**", "/api/**");
    }
}
