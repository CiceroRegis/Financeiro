package com.financeiro.user;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "TBL_USER")
public class User implements Serializable {

	
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull(message = "Nome não informado")
	private String name;

	@NotNull(message = "E-mail não informado")
	@Column(unique = true)
	private String email;

	@Column(unique = true)
	@NotNull(message = "Login não informado")
	private String login;

	@NotNull(message = "Login não informado")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;

	private Date birthday;

	private String fone;

	private String language;

	private boolean active;
	
	public User() {}

	public User(Long id, String name, String email, String login, String password, Date birthday, String fone,
			String language, boolean active) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.login = login;
		this.password = password;
		this.birthday = birthday;
		this.fone = fone;
		this.language = language;
		this.active = active;
	}

	@Override
	public int hashCode() {
		return Objects.hash(active, birthday, email, fone, id, language, login, name, password);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return active == other.active && Objects.equals(birthday, other.birthday) && Objects.equals(email, other.email)
				&& Objects.equals(fone, other.fone) && Objects.equals(id, other.id)
				&& Objects.equals(language, other.language) && Objects.equals(login, other.login)
				&& Objects.equals(name, other.name) && Objects.equals(password, other.password);
	}

}
