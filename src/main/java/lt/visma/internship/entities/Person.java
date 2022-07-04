package lt.visma.internship.entities;

import java.util.Collection;

import java.util.Objects;
import java.util.UUID;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Person implements UserDetails{
	
	/**
	 * 
	 */
	@JsonIgnore
	private static final long serialVersionUID = 1L;

	@JsonProperty
	private UUID id;
	
	@JsonProperty
	@Length(min = 3, max = 20, message = "Vardas turi būti ilgesnis nei 3 simboliai ir trumpesnis nei 20 simbolių")
	private String name;

	@JsonProperty
	@Length(min = 3, max = 20, message = "Pavardė turi būti ilgesnė nei 3 simboliai ir trumpesnė nei 20 simbolių")
	private String surname;
	
	@JsonProperty
	@Email(message="Netinkamas El. paštas", regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@" 
	        + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")
    @NotEmpty(message = "El-paštas privalomas")
	private String email;
	
	@JsonProperty
	@NotEmpty(message = "Slaptažodis privalomas")
	private String password;
	@JsonIgnore
	private String passwordRepeat;
	
	public Person() {
		this.id = UUID.randomUUID();
		
	}
	public Person(String name, String surname, String email, String password, String passwordRepeat) {
		this.id = UUID.randomUUID();
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.password = password;
		this.passwordRepeat = passwordRepeat;
	}


	@JsonIgnore
	public String getPasswordRepeat() {
		return passwordRepeat;
	}



	public void setPasswordRepeat(String passwordRepeat) {
		this.passwordRepeat = passwordRepeat;
	}



	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	@Override
	@JsonIgnore
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	@JsonIgnore
	public String getUsername() {
		return email;
	}

	@Override
	@JsonIgnore
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	@JsonIgnore
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	@JsonIgnore
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	@JsonIgnore
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public int hashCode() {
		return Objects.hash(id, email);
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == this) return true;
		if(!(o instanceof Person)) {
			return false;
		}
		Person person = (Person) o;
		return Objects.equals(id, person.id) &&
				Objects.equals(email, person.email);
	}
	
	
	
	
}
