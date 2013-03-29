package ch.viascom.lusio.beans;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;

import ch.viascom.base.exceptions.ServiceException;

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
	    rouletteBean.processGame();
	    gameDBBean.startNewGame();
	}
}
