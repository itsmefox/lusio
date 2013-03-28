package ch.viascom.lusio.beans;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import ch.viascom.lusio.entity.Game;
import ch.viascom.lusio.entity.Session;
import ch.viascom.lusio.entity.Tip;
import ch.viascom.lusio.entity.User;
import ch.viascom.lusio.module.GameModel;
import ch.viascom.lusio.module.TipModel;

public class GameDBBean {

	@Inject
	EntityManager em;

	public GameModel getGameStats(String gameId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<Game> q = cb.createQuery(Game.class);
		Root<Game> c = q.from(Game.class);

		q.select(c).where(cb.equal(c.get("game_ID"), gameId));

		TypedQuery<Game> query = em.createQuery(q);
		
		Game game = query.getSingleResult();
		
		GameModel gameModel = new GameModel(game);
		
		return gameModel;
	}
	
	
	public List<GameModel> getLatestGames() {
		List<GameModel> games = new ArrayList<>();

		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<Game> q = cb.createQuery(Game.class);
		Root<Game> c = q.from(Game.class);

		q.select(c);
		q.orderBy(cb.asc(c.get("date")));

		TypedQuery<Game> query = em.createQuery(q);

		List<Game> results = query.getResultList();
		if (results.size() != 0) {
			Iterator<Game> stIterator = results.iterator();
			while (stIterator.hasNext()) {
				Game game = (Game) stIterator.next();
				games.add(new GameModel(game));
			}
		}

		return games;
	}

	public List<TipModel> getLatestTips(String gameId) {

		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<Game> q = cb.createQuery(Game.class);
		Root<Game> c = q.from(Game.class);

		q.select(c).where(cb.equal(c.get("game_ID"), gameId));

		TypedQuery<Game> query = em.createQuery(q);

		Game game = query.getSingleResult();

		List<Tip> tips = game.getTips();

		List<TipModel> tipModels = new ArrayList<>();
		
		for (Iterator iterator = tips.iterator(); iterator.hasNext();) {
			Tip tip = (Tip) iterator.next();
			tipModels.add(new TipModel(tip));
			
		}
		
		return tipModels;
	}

	public Game getGame(String gameId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<Game> q = cb.createQuery(Game.class);
		Root<Game> c = q.from(Game.class);

		q.select(c).where(cb.equal(c.get("game_ID"), gameId));

		TypedQuery<Game> query = em.createQuery(q);

		Game game = query.getSingleResult();
		return game;
	}

}
