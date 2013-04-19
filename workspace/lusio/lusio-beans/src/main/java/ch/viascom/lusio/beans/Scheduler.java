package ch.viascom.lusio.beans;

import java.util.Iterator;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;

import ch.viascom.base.exceptions.ServiceException;
import ch.viascom.lusio.entity.Game;

@Singleton
public class Scheduler {

	@Inject
	AccountDBBean accountDBBean;

	@Inject
    RouletteBean rouletteBean;
	
	@Inject
    GameDBBean gameDBBean;
	
	@Schedule(dayOfWeek="*", hour="*", minute="15")
	public void cleanSessions() {
		System.out.println("Start Timer");
		accountDBBean.cleanSession();
	}
	
	@Schedule(dayOfWeek="*", hour="0", minute="0")
	public void processGame() throws ServiceException{
	    // Proccess all OPEN games
	    for (Iterator<Game> iterator = gameDBBean.getOpenGames().iterator(); iterator.hasNext();) {
            Game game = (Game) iterator.next();
            rouletteBean.processGame(game.getGame_ID());
        }
	    gameDBBean.startNewGame();
	}
}
