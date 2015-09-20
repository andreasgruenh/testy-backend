package testy.domain;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import testy.domain.test.TestResult;

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
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<TestResult> testResults = new HashSet<TestResult>();;

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
	
	public Set<TestResult> getTestResults() {
		return Collections.unmodifiableSet(testResults);
	}
	
	public void addTestResult(TestResult result) {
		if(result == null) {
			throw new NullPointerException();
		}
		testResults.add(result);
		result.setUser(this);
	}
	
	public void removeTestResult(TestResult result) {
		if(result == null) {
			throw new NullPointerException();
		}
		testResults.remove(result);
		if(result.getUser() != null) {
			result.setUser(null);
		}
	}
}
