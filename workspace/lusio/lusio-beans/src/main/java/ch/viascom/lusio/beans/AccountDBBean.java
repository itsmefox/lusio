package ch.viascom.lusio.beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.joda.time.DateTime;

import ch.viascom.lusio.entity.Account;
import ch.viascom.lusio.entity.Session;
import ch.viascom.lusio.entity.User;
import ch.viascom.lusio.module.AccountModel;
import ch.viascom.lusio.module.SessionModel;

public class AccountDBBean {

	@Inject
	EntityManager em;

	public final User getDBUser(final String username) {
		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<User> q = cb.createQuery(User.class);
		Root<User> c = q.from(User.class);

		q.select(c).where(cb.equal(c.get("EMail_Address"), username));

		TypedQuery<User> query = em.createQuery(q);

		User user = query.getSingleResult();

		return user;

	}

	public AccountModel getAccountInformations(String sessionId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<Session> q = cb.createQuery(Session.class);
		Root<Session> c = q.from(Session.class);

		q.select(c).where(cb.equal(c.get("session_ID"), sessionId));

		TypedQuery<Session> query = em.createQuery(q);

		User user = query.getSingleResult().getUser();

		AccountModel accountModel = new AccountModel(user);

		return accountModel;
	}
	
	public User getUser(String userId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<User> q = cb.createQuery(User.class);
		Root<User> c = q.from(User.class);

		q.select(c).where(cb.equal(c.get("user_ID"), userId));

		TypedQuery<User> query = em.createQuery(q);

		User user = query.getSingleResult();

		return user;
	}

	public double getCredit(String userId) {
		
		double credit = 0;
		
		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<User> q = cb.createQuery(User.class);
		Root<User> c = q.from(User.class);

		q.select(c).where(cb.equal(c.get("user_ID"), userId));

		TypedQuery<User> query = em.createQuery(q);

		User user = query.getSingleResult();
		
		List<Account> accounts = user.getAccounts();
		
		for (Iterator iterator = accounts.iterator(); iterator.hasNext();) {
			Account account = (Account) iterator.next();
			credit += account.getCredit();
		}
		
		return credit;
	}

	public final SessionModel createSessionId(User user, String ipAddress) {
		String guid = UUID.randomUUID().toString();

		SessionModel sessionModel = new SessionModel();

		sessionModel.setSessionId(guid);

		Session session = new Session();
		session.setDate(DateTime.now().toDate());
		session.setSession_ID(guid);
		session.setUser(user);

		List<Session> sessions = new ArrayList<>();
		sessions.add(session);

		em.getTransaction().begin();

		Session sessionRow = new Session();
		sessionRow.setSession_ID(guid);
		sessionRow.setUser(user);
		sessionRow.setIP_Address(ipAddress);
		sessionRow.setDate(new Date());

		em.persist(sessionRow);

		em.getTransaction().commit();

		return sessionModel;
	}

	public boolean logout(String sessionId) {
		Boolean sessionSuccessfullDeleted = true;

		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<Session> q = cb.createQuery(Session.class);
		Root<Session> c = q.from(Session.class);

		q.select(c).where(cb.equal(c.get("session_ID"), sessionId));

		TypedQuery<Session> query = em.createQuery(q);

		Session session = null;

		try {
			session = query.getSingleResult();

			em.getTransaction().begin();
			em.remove(session);
			em.getTransaction().commit();

		} catch (Exception e) {
			return false;
		}

		return sessionSuccessfullDeleted;
	}

	public final void cleanSession() {
		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<Session> q = cb.createQuery(Session.class);
		Root<Session> c = q.from(Session.class);

		q.select(c).where(cb.lessThan(c.<Date> get("Date"), new Date()));

		TypedQuery<Session> query = em.createQuery(q);

		List<Session> results = query.getResultList();
		if (results.size() != 0) {
			Iterator<Session> stIterator = results.iterator();
			while (stIterator.hasNext()) {
				Session session = (Session) stIterator.next();
				em.getTransaction().begin();
				em.remove(session);
				em.getTransaction().commit();
			}
		}
	}
}
