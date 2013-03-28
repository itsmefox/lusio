package ch.viascom.lusio.entity;

import java.io.Serializable;
import javax.persistence.*;

import java.util.Date;


/**
 * The persistent class for the Session database table.
 * 
 */
@Entity
@Cacheable(false)
public class Session implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String session_ID;

	@Temporal(TemporalType.DATE)
	private Date date;

	private String IP_Address;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="User_FK")
	private User user;

	public Session() {
	}

	public String getSession_ID() {
		return this.session_ID;
	}

	public void setSession_ID(String session_ID) {
		this.session_ID = session_ID;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getIP_Address() {
		return this.IP_Address;
	}

	public void setIP_Address(String IP_Address) {
		this.IP_Address = IP_Address;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}