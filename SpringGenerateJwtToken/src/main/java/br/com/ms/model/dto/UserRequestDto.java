package br.com.ms.model.dto;

import java.io.Serializable;

import org.springframework.stereotype.Component;

@Component
public class UserRequestDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private String username;
	private String password;

	public UserRequestDto() {
	}

	public UserRequestDto(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "UserRequestDto [username=" + username + ", password=" + password + "]";
	}

}
