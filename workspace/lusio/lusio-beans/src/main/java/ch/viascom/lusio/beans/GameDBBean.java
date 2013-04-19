package ch.viascom.lusio.beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import ch.viascom.base.exceptions.ServiceException;
import ch.viascom.lusio.entity.Field;
import ch.viascom.lusio.entity.Game;
import ch.viascom.lusio.entity.Tip;
import ch.viascom.lusio.module.GameModel;
import ch.viascom.lusio.module.TipModel;

public class GameDBBean {

	@Inject
	EntityManager em;

	public GameModel getGameStats(String gameId) throws ServiceException {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();

			CriteriaQuery<Game> q = cb.createQuery(Game.class);
			Root<Game> c = q.from(Game.class);

			q.select(c).where(cb.equal(c.get("game_ID"), gameId));

			TypedQuery<Game> query = em.createQuery(q);
			query.setHint("javax.persistence.cache.storeMode", "REFRESH");

			Game game = query.getSingleResult();

			GameModel gameModel = new GameModel(game);

			gameModel.setIncome(getIncome(gameId));

			return gameModel;
		} catch (NoResultException e) {
			throw new ServiceException("NO_RESULT_EXCEPTION",
					"There ist no game with this id.")
					.setResponseStatusCode(404);
		}
	}

	public List<GameModel> getLatestGames() throws ServiceException {
		List<GameModel> games = new ArrayList<>();

		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<Game> q = cb.createQuery(Game.class);
		Root<Game> c = q.from(Game.class);

		q.select(c);
		q.orderBy(cb.asc(c.get("date")));

		TypedQuery<Game> query = em.createQuery(q);
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");

		List<Game> results = query.getResultList();
		if (results.size() != 0) {
			Iterator<Game> stIterator = results.iterator();
			while (stIterator.hasNext()) {
				Game game = (Game) stIterator.next();
				GameModel gameModel = new GameModel(game);
				gameModel.setIncome(getIncome(game.getGame_ID()));
				games.add(gameModel);
			}
		}

		return games;
	}

	public List<TipModel> getLatestTips(String gameId) throws ServiceException {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();

			CriteriaQuery<Game> q = cb.createQuery(Game.class);
			Root<Game> c = q.from(Game.class);

			q.select(c).where(cb.equal(c.get("game_ID"), gameId));

			TypedQuery<Game> query = em.createQuery(q);
			query.setHint("javax.persistence.cache.storeMode", "REFRESH");

			Game game = query.getSingleResult();

			List<Tip> tips = game.getTips();

			List<TipModel> tipModels = new ArrayList<>();

			for (Iterator<Tip> iterator = tips.iterator(); iterator.hasNext();) {
				Tip tip = (Tip) iterator.next();
				tipModels.add(new TipModel(tip));

			}
			return tipModels;
		} catch (NoResultException e) {
			throw new ServiceException("NO_RESULT_EXCEPTION",
					"There are no games.")
					.setResponseStatusCode(404);
		}
	}

	public Game getGame(String gameId) throws ServiceException {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();

			CriteriaQuery<Game> q = cb.createQuery(Game.class);
			Root<Game> c = q.from(Game.class);

			q.select(c).where(cb.equal(c.get("game_ID"), gameId));

			TypedQuery<Game> query = em.createQuery(q);

			Game game = query.getSingleResult();
			return game;
		} catch (NoResultException e) {
			throw new ServiceException("NO_RESULT_EXCEPTION",
					"There ist no game with this id.")
					.setResponseStatusCode(404);
		}
	}

	public int getIncome(String gameId) throws ServiceException {
		List<TipModel> tips = getLatestTips(gameId);

		int income = 0;

		for (Iterator<TipModel> iterator = tips.iterator(); iterator.hasNext();) {
			TipModel tipModel = (TipModel) iterator.next();
			income += tipModel.getAmount();
		}

		return income;
	}
	
	public List<Game> getOpenGames() throws ServiceException{
		try {
		    List<Game> games = new ArrayList<>();
			CriteriaBuilder cb = em.getCriteriaBuilder();

			CriteriaQuery<Game> q = cb.createQuery(Game.class);
			Root<Game> c = q.from(Game.class);

			q.select(c).where(cb.equal(c.get("status"), 0));

			TypedQuery<Game> query = em.createQuery(q);

			query.setHint("javax.persistence.cache.storeMode", "REFRESH");

	        List<Game> results = query.getResultList();
	        if (results.size() != 0) {
	            Iterator<Game> stIterator = results.iterator();
	            while (stIterator.hasNext()) {
	                Game game = (Game) stIterator.next();
	                games.add(game);
	            }
	        }  
			return games;
		} catch (NoResultException e) {
			throw new ServiceException("NO_RESULT_EXCEPTION",
					"There ist no open-games.")
					.setResponseStatusCode(404);
		}
	}
	
	public Field getField(String value) throws ServiceException{
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();

			CriteriaQuery<Field> q = cb.createQuery(Field.class);
			Root<Field> c = q.from(Field.class);

			q.select(c).where(cb.equal(c.get("value"), value));

			TypedQuery<Field> query = em.createQuery(q);

			Field field = query.getSingleResult();
			return field;
		} catch (NoResultException e) {
			throw new ServiceException("NO_RESULT_EXCEPTION",
					"There ist no field with this id.")
					.setResponseStatusCode(404);
		}
	}

	public boolean isGameOpen(String gameId) throws ServiceException {
		Game game = getGame(gameId);
		if (game.getStatus() == 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public void setOutgoing(String gameId, int outgoing) throws ServiceException{
	    em.getTransaction().begin();
       
	    Game game = getGame(gameId);
	    game.setOutgoing(outgoing);
        em.merge(game);
        
        em.getTransaction().commit();
	}
	
	public void setIncome(String gameId, int income) throws ServiceException{
        em.getTransaction().begin();
       
        Game game = getGame(gameId);
        game.setIncome(income);
        em.merge(game);
        
        em.getTransaction().commit();
    }
	
	public void setWinField(String gameId,Field field) throws ServiceException{
	    em.getTransaction().begin();
	       
        Game game = getGame(gameId);
        game.setField(field);
        field.getGames().add(game);
        em.merge(field);
        em.merge(game);
        
        em.getTransaction().commit();
	}
	
	public void startNewGame(){
	    em.getTransaction().begin();
        
        Game game = new Game();
        game.setDate(new Date());
        game.setStatus(0);
        game.setGame_ID(UUID.randomUUID().toString());
        em.persist(game);
        em.getTransaction().commit();
	}

}
