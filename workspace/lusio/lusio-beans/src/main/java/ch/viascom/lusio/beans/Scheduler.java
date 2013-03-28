package ch.viascom.lusio.beans;

import javax.ejb.Schedule;
import javax.inject.Inject;

public class Scheduler {

	@Inject
	AccountDBBean accountDBBean;

	@Schedule(hour = "0", minute = "17", dayOfWeek = "*")
	public void cleanSessions() {
		accountDBBean.cleanSession();
	}
}
