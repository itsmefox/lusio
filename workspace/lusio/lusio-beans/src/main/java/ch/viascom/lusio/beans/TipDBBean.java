package ch.viascom.lusio.beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
import javax.persistence.PessimisticLockException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import ch.viascom.base.exceptions.ServiceException;
import ch.viascom.lusio.entity.Account;
import ch.viascom.lusio.entity.Account_log;
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

    public String createNewTip(String fieldId, String gameId, int amount, String userId) throws ServiceException {

        if (gameDBBean.isGameOpen(gameId)) {

            try {
                em.getTransaction().begin();

                CriteriaBuilder cb = em.getCriteriaBuilder();

                CriteriaQuery<User> q = cb.createQuery(User.class);
                Root<User> c = q.from(User.class);

                q.select(c).where(cb.equal(c.get("user_ID"), userId));

                TypedQuery<User> query = em.createQuery(q);

                User user = query.getSingleResult();

                List<Account> accounts = user.getAccounts();

                for (Iterator<Account> iterator = accounts.iterator(); iterator.hasNext();) {
                    Account account = (Account) iterator.next();
                    // Lock
                    em.lock(account, LockModeType.PESSIMISTIC_READ);

                    // if enough to create tip?
                    if (account.getCredit() >= amount) {

                        int newAmount = account.getCredit() - amount;

                        Account_log log = new Account_log();
                        log.setAccount(account);
                        log.setDate(new Date());
                        log.setOld_value(account.getCredit());
                        log.setNew_value(newAmount);
                        log.setAccount_log_ID(UUID.randomUUID().toString());

                        account.setCredit(newAmount);
                        account.getAccountLogs().add(log);
                        em.merge(account);
                        em.persist(log);

                        Game game = gameDBBean.getGame(gameId);

                        Field field = getField(fieldId);

                        Tip newTip = new Tip();
                        newTip.setAmount(amount);
                        newTip.setDate(new Date());
                        newTip.setField(field);
                        newTip.setGame(game);
                        newTip.setUser(accountDBBean.getUser(userId));
                        newTip.setTip_ID(UUID.randomUUID().toString());

                        user.getTips().add(newTip);
                        game.getTips().add(newTip);
                        field.getTips().add(newTip);

                        em.merge(user);
                        em.merge(game);
                        em.persist(newTip);

                        em.getTransaction().commit();

                        return newTip.getTip_ID();

                    } else {
                        em.getTransaction().rollback();
                        throw new ServiceException("NOT_ENOUGH_CREDIT", "").setResponseStatusCode(404);
                    }
                }
                throw new ServiceException("NOT_ENOUGH_CREDIT", "").setResponseStatusCode(404);
            } catch (PessimisticLockException e) {
                throw new ServiceException("OptimisticLockException", "").setException(e).setResponseStatusCode(404);
            }
        } else {
            throw new ServiceException("GAME_IS_NOT_OPEN", "").setResponseStatusCode(404);
        }

    }

    public boolean removeTip(String tipId, String userId) throws ServiceException {

        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<Tip> q = cb.createQuery(Tip.class);
        Root<Tip> c = q.from(Tip.class);

        q.select(c).where(cb.equal(c.get("tip_ID"), tipId));

        TypedQuery<Tip> query = em.createQuery(q);

        Tip tip = null;

        tip = query.getSingleResult();

        if (gameDBBean.isGameOpen(tip.getGame().getGame_ID())) {
            Boolean sessionSuccessfullDeleted = true;
            try {
                em.getTransaction().begin();

                CriteriaBuilder cb2 = em.getCriteriaBuilder();

                CriteriaQuery<User> q2 = cb.createQuery(User.class);
                Root<User> c2 = q.from(User.class);

                q2.select(c2).where(cb2.equal(c2.get("user_ID"), userId));

                TypedQuery<User> query2 = em.createQuery(q2);

                User user = query2.getSingleResult();

                List<Account> accounts = user.getAccounts();
                for (Iterator<Account> iterator = accounts.iterator(); iterator.hasNext();) {
                    Account account = (Account) iterator.next();
                    em.lock(account, LockModeType.PESSIMISTIC_READ);

                    int newAmount = account.getCredit() + tip.getAmount();

                    Account_log log = new Account_log();
                    log.setAccount(account);
                    log.setDate(new Date());
                    log.setOld_value(account.getCredit());
                    log.setNew_value(newAmount);
                    log.setAccount_log_ID(UUID.randomUUID().toString());

                    account.setCredit(newAmount);
                    account.getAccountLogs().add(log);
                    em.merge(account);
                    em.persist(log);

                }
                em.remove(tip);
                em.getTransaction().commit();

            } catch (NoResultException e) {
                throw new ServiceException("NO_RESULT_EXCEPTION", "There ist no tip with this id.").setResponseStatusCode(404);
            } catch (Exception e) {
                throw new ServiceException("", "").setResponseStatusCode(404).setException(e);
            }

            return sessionSuccessfullDeleted;
        } else {
            throw new ServiceException("GAME_IS_NOT_OPEN", "").setResponseStatusCode(404);
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

    public List<Tip> getTipsByField(String fieldId, String gameId) {
        
        List<Tip> tips = new ArrayList<>();
        
        for (Iterator<Tip> iterator = getField(fieldId).getTips().iterator(); iterator.hasNext();) {
            Tip tip = (Tip) iterator.next();
            if(tip.getGame().getGame_ID() == gameId){
                tips.add(tip);
            }
        }
        return tips;
    }
    
    
}
