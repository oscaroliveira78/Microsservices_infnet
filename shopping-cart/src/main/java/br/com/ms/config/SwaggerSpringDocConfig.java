package br.com.ms.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class SwaggerSpringDocConfig {

	@Autowired
	private ApplicationProperties properties;

    @Bean
    public OpenAPI springOpenAPI() {
        return new OpenAPI()
                .info(info())
                .components(new Components());
    }
    
    private Info info() {
        return new Info()
                .title(properties.getNome())
                .description(properties.getDescricao())
                .version(properties.getVersao())
                .license(license())
                .contact(contact());
    }
    
    private Contact contact() {
    	Contact contact = new Contact();
        contact.setName("Oscar");
        contact.setUrl("meuGit");
        contact.setEmail("oscarroliveira@gmail.com");
        
        return contact;
    }
    
    private License license() {
    	return new License()
                .name("N/A")
                .url("N/A");
    }
}
