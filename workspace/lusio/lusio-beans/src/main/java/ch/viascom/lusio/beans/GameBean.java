package ch.viascom.lusio.beans;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;

import ch.viascom.lusio.entity.Tip;
import ch.viascom.lusio.module.GameModel;
import ch.viascom.lusio.module.TipModel;

@Stateless
public class GameBean {

	@Inject
	GameDBBean gameDBBean;
	
	public GameModel getGameStats(String gameId) {
		return gameDBBean.getGameStats(gameId);
	}
	
	public List<GameModel> getLatestGames(){
		return gameDBBean.getLatestGames(); 
	}
	
	public List<TipModel> getLatestTips(String gameId){
		return gameDBBean.getLatestTips(gameId);
	}
}
