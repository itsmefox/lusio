package ch.viascom.lusio.beans;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import ch.viascom.base.exceptions.ServiceException;
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
					"There ist no tip with this id.")
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
					"There ist no tip with this id.")
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
					"There ist no tip with this id.")
					.setResponseStatusCode(404);
		}
	}

	public double getIncome(String gameId) throws ServiceException {
		List<TipModel> tips = getLatestTips(gameId);

		double income = 0;

		for (Iterator<TipModel> iterator = tips.iterator(); iterator.hasNext();) {
			TipModel tipModel = (TipModel) iterator.next();
			income += tipModel.getAmount();
		}

		return income;
	}

}
