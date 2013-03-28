package ch.viascom.lusio.beans;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;

@Singleton
public class Scheduler {

	@Inject
	AccountDBBean accountDBBean;

	@Schedule(dayOfWeek="*", hour="*", minute="15")
	public void cleanSessions() {
		System.out.println("Start Timer");
		accountDBBean.cleanSession();
	}
}
