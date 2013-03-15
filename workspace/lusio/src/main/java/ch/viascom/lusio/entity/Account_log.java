package ch.viascom.lusio.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the Account_log database table.
 * 
 */
@Entity
public class Account_log implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String account_log_ID;

	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	private BigDecimal new_value;

	private BigDecimal old_value;

	//bi-directional many-to-one association to Account
	@ManyToOne
	@JoinColumn(name="Account_FK")
	private Account account;

	public Account_log() {
	}

	public String getAccount_log_ID() {
		return this.account_log_ID;
	}

	public void setAccount_log_ID(String account_log_ID) {
		this.account_log_ID = account_log_ID;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public BigDecimal getNew_value() {
		return this.new_value;
	}

	public void setNew_value(BigDecimal new_value) {
		this.new_value = new_value;
	}

	public BigDecimal getOld_value() {
		return this.old_value;
	}

	public void setOld_value(BigDecimal old_value) {
		this.old_value = old_value;
	}

	public Account getAccount() {
		return this.account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

}