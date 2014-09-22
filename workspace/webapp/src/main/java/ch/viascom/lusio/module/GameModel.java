package ch.viascom.lusio.module;

import lombok.Data;

import org.joda.time.DateTime;


@Data
public class GameModel {
	private String id;
	private DateTime date;
	private FieldModel field;
	private GameStatus status;
	private double income;
	private double outgonig;

	
}
