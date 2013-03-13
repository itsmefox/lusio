package ch.viascom.lusio.beans;

import java.util.List;
import javax.ejb.Stateless;
import ch.viascom.lusio.module.GameModel;

@Stateless
public class GameBean {

	public GameModel getGameStats(String gameId) {
		return null;
	}
	
	public List<String> getLatestGames(){
		return null;
	}
}
