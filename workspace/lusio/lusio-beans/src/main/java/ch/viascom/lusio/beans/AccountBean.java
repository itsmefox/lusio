package ch.viascom.lusio.beans;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import ch.viascom.lusio.entity.Session;
import ch.viascom.lusio.entity.User;
import ch.viascom.lusio.module.AccountModel;
import ch.viascom.lusio.module.SessionModel;
import ch.viascom.lusio.util.LangUtil;
import ch.viascom.lusio.util.Security;

@Stateless
public class AccountBean {

	@Inject
	EntityManager em;

	@Inject
	AccountDBBean accountDBBean;

	public SessionModel login(final String username, final String password,
			final String ipAddress) {

		User user = accountDBBean.getDBUser(username);
		String dbPassword = user.getPassword();

		SessionModel session = null;

		if (LangUtil.saveEquals(dbPassword, Security.MD5(password))) {
			// Create Session
			return accountDBBean.createSessionId(user, ipAddress);

		}

		return session;
	}

	public boolean logout(String sessionId) {
		return accountDBBean.logout(sessionId);
	}

	public double getCredit(String userId){
		return accountDBBean.getCredit(userId);
	}

	public AccountModel getAccountInformations(String sessionId) {
		return accountDBBean.getAccountInformations(sessionId);
	}

	public boolean isAuthorized(String sessionId, String ipAddress) {
		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<Session> q = cb.createQuery(Session.class);
		Root<Session> c = q.from(Session.class);

		q.select(c).where(
				cb.and(cb.equal(c.get("session_ID"), sessionId),
						cb.equal(c.get("IP_Address"), ipAddress)));

		TypedQuery<Session> query = em.createQuery(q);

		Session session = null;

		try {
			session = query.getSingleResult();
		} catch (Exception e) {
			return false;
		}
		return (session != null) ? true : false;
	}
}
