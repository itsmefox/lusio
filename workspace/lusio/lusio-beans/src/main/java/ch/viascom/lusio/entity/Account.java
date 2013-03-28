package ch.viascom.lusio.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the Account database table.
 * 
 */
@Entity
@Cacheable(false)
public class Account implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String account_ID;

	private int credit;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="User_FK")
	private User user;

	//bi-directional many-to-one association to Account_log
	@OneToMany(mappedBy="account")
	private List<Account_log> accountLogs;

	public Account() {
	}

	public String getAccount_ID() {
		return this.account_ID;
	}

	public void setAccount_ID(String account_ID) {
		this.account_ID = account_ID;
	}

	public int getCredit() {
		return this.credit;
	}

	public void setCredit(int credit) {
		this.credit = credit;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Account_log> getAccountLogs() {
		return this.accountLogs;
	}

	public void setAccountLogs(List<Account_log> accountLogs) {
		this.accountLogs = accountLogs;
	}

}