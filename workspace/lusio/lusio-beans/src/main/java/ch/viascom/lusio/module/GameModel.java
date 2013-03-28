package ch.viascom.lusio.module;

import lombok.Data;

import org.joda.time.DateTime;

import ch.viascom.lusio.entity.Game;

@Data
public class GameModel {
	private String id;
	private DateTime date;
	private FieldModel field;
	private GameStatus status;
	private double income;
	private double outgonig;

	public GameModel(Game game) {
		id = game.getGame_ID();
		date = new DateTime(game.getDate());
		field = (game.getField() != null) ? new FieldModel(game.getField())
				: null;
		outgonig = game.getOutgoing();

		switch (game.getStatus()) {
		case 0:
		default:
			status = GameStatus.OPEN;
			break;
		case 1:
			status = GameStatus.PROCESSING;
			break;
		case 2:
			status = GameStatus.CLOSED;
			break;
		}
	}
}
