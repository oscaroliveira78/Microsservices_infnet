package br.com.ms.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "propriedades.aplicacao")
public class ApplicationProperties {

	private String nome;
	private String versao;
	private String descricao;

}
