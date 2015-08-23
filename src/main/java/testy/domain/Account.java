package testy.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Account {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(unique=true, nullable=false)
	private String accountName;
	
	private boolean isAdmin;
	
	private String firstname, lastname;
	
	private String email;

	public Account() {
		isAdmin = false;
	}
	
	public Account(String accountName) {
		this.accountName = accountName;
		isAdmin = false;
	}
	
	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		if(accountName.equals("aroth")) {
			this.isAdmin = true;
		} else {
			this.isAdmin = isAdmin;
		}
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public long getId() {
		return id;
	}

	public String getAccountName() {
		return accountName;
	}
}
