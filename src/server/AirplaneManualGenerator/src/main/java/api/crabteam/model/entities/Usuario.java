package api.crabteam.model.entities;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import api.crabteam.converters.PasswordConverter;
import api.crabteam.utils.Password;

@Entity(name = "usuario")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Usuario {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(nullable = false)
	private String nome;
	
	@Column(nullable = false, unique = true)
	private String email;
	
	@Column(nullable = false, columnDefinition = "text")
	@Convert(converter = PasswordConverter.class)
	private Password senha;
	
	public Usuario() {
		
	}
	
	public Usuario(String nome, String email, String senha) {
		this.nome = nome;
		this.email = email;
		this.setSenha(senha);
		
	}
	
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public Password getSenha() {
		return senha;
	}
	
	public void setSenha(String senha) {
		Password password = new Password();
		
		password.setValue(senha);
		
		this.senha = password;
		
	}

}
