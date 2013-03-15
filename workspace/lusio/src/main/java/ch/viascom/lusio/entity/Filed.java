package ch.viascom.lusio.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the Filed database table.
 * 
 */
@Entity
public class Filed implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String field_ID;

	private String color;

	private String value;

	//bi-directional many-to-one association to Tip
	@OneToMany(mappedBy="filed")
	private List<Tip> tips;

	//bi-directional many-to-one association to Game
	@OneToMany(mappedBy="filed")
	private List<Game> games;

	public Filed() {
	}

	public String getField_ID() {
		return this.field_ID;
	}

	public void setField_ID(String field_ID) {
		this.field_ID = field_ID;
	}

	public String getColor() {
		return this.color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public List<Tip> getTips() {
		return this.tips;
	}

	public void setTips(List<Tip> tips) {
		this.tips = tips;
	}

	public List<Game> getGames() {
		return this.games;
	}

	public void setGames(List<Game> games) {
		this.games = games;
	}

}