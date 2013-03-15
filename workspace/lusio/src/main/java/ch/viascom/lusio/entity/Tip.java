package ch.viascom.lusio.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the Tip database table.
 * 
 */
@Entity
public class Tip implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String tip_ID;

	private BigDecimal amount;

	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="User_FK")
	private User user;

	//bi-directional many-to-one association to Game
	@ManyToOne
	@JoinColumn(name="Game_FK")
	private Game game;

	//bi-directional many-to-one association to Filed
	@ManyToOne
	@JoinColumn(name="Field_FK")
	private Filed filed;

	public Tip() {
	}

	public String getTip_ID() {
		return this.tip_ID;
	}

	public void setTip_ID(String tip_ID) {
		this.tip_ID = tip_ID;
	}

	public BigDecimal getAmount() {
		return this.amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Game getGame() {
		return this.game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public Filed getFiled() {
		return this.filed;
	}

	public void setFiled(Filed filed) {
		this.filed = filed;
	}

}