package ch.viascom.lusio.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the User database table.
 * 
 */
@Entity
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String user_ID;

	private String EMail_Address;

	private String first_Name;

	private String last_Name;

	private String password;

	//bi-directional many-to-one association to Session
	@OneToMany(mappedBy="user")
	private List<Session> sessions;

	//bi-directional many-to-one association to Account
	@OneToMany(mappedBy="user")
	private List<Account> accounts;

	//bi-directional many-to-one association to Tip
	@OneToMany(mappedBy="user")
	private List<Tip> tips;

	public User() {
	}

	public String getUser_ID() {
		return this.user_ID;
	}

	public void setUser_ID(String user_ID) {
		this.user_ID = user_ID;
	}

	public String getEMail_Address() {
		return this.EMail_Address;
	}

	public void setEMail_Address(String EMail_Address) {
		this.EMail_Address = EMail_Address;
	}

	public String getFirst_Name() {
		return this.first_Name;
	}

	public void setFirst_Name(String first_Name) {
		this.first_Name = first_Name;
	}

	public String getLast_Name() {
		return this.last_Name;
	}

	public void setLast_Name(String last_Name) {
		this.last_Name = last_Name;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Session> getSessions() {
		return this.sessions;
	}

	public void setSessions(List<Session> sessions) {
		this.sessions = sessions;
	}

	public List<Account> getAccounts() {
		return this.accounts;
	}

	public void setAccounts(List<Account> accounts) {
		this.accounts = accounts;
	}

	public List<Tip> getTips() {
		return this.tips;
	}

	public void setTips(List<Tip> tips) {
		this.tips = tips;
	}

}