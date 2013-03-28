package ch.viascom.lusio.beans;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PessimisticLockException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import ch.viascom.base.exceptions.ServiceException;
import ch.viascom.lusio.entity.Account;
import ch.viascom.lusio.entity.Field;
import ch.viascom.lusio.entity.Game;
import ch.viascom.lusio.entity.Tip;
import ch.viascom.lusio.entity.User;

public class TipDBBean {

	@Inject
	EntityManager em;

	@Inject
	GameDBBean gameDBBean;

	@Inject
	AccountDBBean accountDBBean;

	public String createNewTip(String tipId, String fieldId, String gameId,
			int amount, String userId) throws ServiceException {
		try {
			em.getTransaction().begin();

			CriteriaBuilder cb = em.getCriteriaBuilder();

			CriteriaQuery<User> q = cb.createQuery(User.class);
			Root<User> c = q.from(User.class);

			q.select(c).where(cb.equal(c.get("user_ID"), userId));

			TypedQuery<User> query = em.createQuery(q);

			User user = query.getSingleResult();

			List<Account> accounts = user.getAccounts();

			for (Iterator iterator = accounts.iterator(); iterator.hasNext();) {
				Account account = (Account) iterator.next();
				// Lock
				em.lock(account, LockModeType.PESSIMISTIC_READ);

				// if enough to create tip?
				if (account.getCredit() >= amount) {
					account.setCredit(account.getCredit() - amount);
					em.merge(account);

					Game game = gameDBBean.getGame(gameId);
					em.lock(game, LockModeType.PESSIMISTIC_READ);
					
					Field field = getField(fieldId);
					System.out.println(field.getValue());
					
					Tip newTip = new Tip();
					newTip.setAmount(new BigDecimal(amount));
					newTip.setDate(new Date());
					newTip.setField(field);
					newTip.setGame(game);
					newTip.setUser(accountDBBean.getUser(userId));
					newTip.setTip_ID(UUID.randomUUID().toString());

					user.getTips().add(newTip);
					
					game.getTips().add(newTip);
					game.setIncome(game.getIncome()+amount);
					
					field.getTips().add(newTip);
					
					em.merge(user);
					em.merge(game);
					em.persist(newTip);

					em.getTransaction().commit();

					return newTip.getTip_ID();

				} else {
					em.getTransaction().rollback();
					throw new ServiceException("NOT_ENOUGH_CREDIT", "")
							.setResponseStatusCode(404);
				}
			}
			throw new ServiceException("NOT_ENOUGH_CREDIT", "")
					.setResponseStatusCode(404);
		} catch (PessimisticLockException e) {
			throw new ServiceException("OptimisticLockException", "")
					.setException(e).setResponseStatusCode(404);
		}

	}

	public Field getField(String fieldId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<Field> q = cb.createQuery(Field.class);
		Root<Field> c = q.from(Field.class);

		q.select(c).where(cb.equal(c.get("field_ID"), fieldId));

		TypedQuery<Field> query = em.createQuery(q);

		Field field = query.getSingleResult();
		return field;
	}
}
