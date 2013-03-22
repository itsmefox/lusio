package ch.viascom.lusio.beans;

import java.util.UUID;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.joda.time.DateTime;

import ch.viascom.lusio.entity.Session;
import ch.viascom.lusio.entity.User;
import ch.viascom.lusio.module.SessionModel;

public class AccountDBBean {

	@Inject
	EntityManager em;

	public final User getDBUser(final String username) {
		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<User> q = cb.createQuery(User.class);
		Root<User> c = q.from(User.class);

		q.select(c).where(cb.equal(c.get("username"), username));

		TypedQuery<User> query = em.createQuery(q);

		User user = query.getSingleResult();

		return user;

	}

	public final SessionModel createSessionId(User user) {
		String guid = UUID.randomUUID().toString();

		SessionModel sessionModel = new SessionModel();
		
		sessionModel.setSessionId(guid);
		
		
		Session session = new Session();
		session.setDate(DateTime.now().toDate());
		session.setSession_ID(guid);
		session.setUser(user);
		
		em.getTransaction().begin();
		em.persist(session);
		em.getTransaction().commit();
		em.close();
		
		return sessionModel;
	}
}
