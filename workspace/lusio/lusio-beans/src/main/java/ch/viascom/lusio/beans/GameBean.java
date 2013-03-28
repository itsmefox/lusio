package ch.viascom.lusio.beans;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;

import ch.viascom.base.exceptions.ServiceException;
import ch.viascom.lusio.module.GameModel;
import ch.viascom.lusio.module.TipModel;

@Stateless
public class GameBean {

	@Inject
	GameDBBean gameDBBean;
	
	public GameModel getGameStats(String gameId) throws ServiceException {
		return gameDBBean.getGameStats(gameId);
	}
	
	public List<GameModel> getLatestGames() throws ServiceException{
		return gameDBBean.getLatestGames(); 
	}
	
	public List<TipModel> getLatestTips(String gameId) throws ServiceException{
		return gameDBBean.getLatestTips(gameId);
	}
}
