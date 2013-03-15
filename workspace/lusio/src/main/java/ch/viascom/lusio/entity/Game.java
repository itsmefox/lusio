package ch.viascom.lusio.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the Game database table.
 * 
 */
@Entity
public class Game implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String game_ID;

	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	private int status;

	//bi-directional many-to-one association to Tip
	@OneToMany(mappedBy="game")
	private List<Tip> tips;

	//bi-directional many-to-one association to Filed
	@ManyToOne
	@JoinColumn(name="Field_FK")
	private Filed filed;

	public Game() {
	}

	public String getGame_ID() {
		return this.game_ID;
	}

	public void setGame_ID(String game_ID) {
		this.game_ID = game_ID;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public List<Tip> getTips() {
		return this.tips;
	}

	public void setTips(List<Tip> tips) {
		this.tips = tips;
	}

	public Filed getFiled() {
		return this.filed;
	}

	public void setFiled(Filed filed) {
		this.filed = filed;
	}

}