package ch.viascom.lusio.module;

import org.joda.time.DateTime;

import lombok.Data;

@Data
public class TipModel {
	private String tip_ID;

	private int amount;

	private DateTime date;

	private FieldModel field;

	private GameModel game;

	private AccountModel user;
}
