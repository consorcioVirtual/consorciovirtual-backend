package ar.edu.unsam.consorciovirtual.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${ar.edu.unsam.consorcio-virtual.frontend}")
    private String urlCrossOriginFrontend;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/registroModificacion/**")
                .allowedOrigins(urlCrossOriginFrontend);
    }

}
