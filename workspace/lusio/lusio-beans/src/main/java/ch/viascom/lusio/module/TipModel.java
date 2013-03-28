package ch.viascom.lusio.module;

import java.math.BigDecimal;

import org.joda.time.DateTime;

import lombok.Data;

import ch.viascom.lusio.entity.Tip;

@Data
public class TipModel {
	private String tip_ID;

	private BigDecimal amount;

	private DateTime date;

	private FieldModel field;

	private GameModel game;

	private AccountModel user;

	public TipModel(Tip tip) {
		tip_ID = tip.getTip_ID();
		amount = tip.getAmount();
		date = new DateTime(tip.getDate());
		field = new FieldModel(tip.getField());
		game = new GameModel(tip.getGame());
		user = new AccountModel(tip.getUser());
	}
}
