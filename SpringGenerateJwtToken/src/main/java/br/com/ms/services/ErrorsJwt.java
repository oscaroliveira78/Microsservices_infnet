package br.com.ms.services;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;

@Component
public class ErrorsJwt {

	public TabelaDeErros buildError(Class<? extends Exception> classeDeErro) {

		if (SignatureException.class.isAssignableFrom(classeDeErro)) {
			return TabelaDeErros.ASSINATURA_INVALIDA;
		}
		if (MalformedJwtException.class.isAssignableFrom(classeDeErro)) {
			return TabelaDeErros.TOKEN_INVALIDO;
		}
		if (ExpiredJwtException.class.isAssignableFrom(classeDeErro)) {
			return TabelaDeErros.TOKEN_EXPIRADO;
		}
		if (UnsupportedJwtException.class.isAssignableFrom(classeDeErro)) {
			return TabelaDeErros.TOKEN_NAO_SUPORTADO;
		}
		if (IllegalArgumentException.class.isAssignableFrom(classeDeErro)) {
			return TabelaDeErros.TOKEN_CLAINS_NULL;
		}

		return TabelaDeErros.ERRO_GENERICO;

	}

}
