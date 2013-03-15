package ch.viascom.lusio;

import java.util.List;

import javax.annotation.ManagedBean;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import ch.viascom.lusio.base.exceptions.ServiceResult;
import ch.viascom.lusio.base.exceptions.ServiceResultStatus;
import ch.viascom.lusio.beans.GameBean;
import ch.viascom.lusio.module.GameModel;

@ManagedBean
@Path("{sessionId}/game")
public class Game {

	@EJB
	GameBean gameBean;
	
	@GET
	@Path("/{gameId}/stats")
	@Produces("application/json;charset=UTF-8")
	public ServiceResult<GameModel> getGameStats(@Context UriInfo url,
			@PathParam("sessionId") String sessionId,
			@PathParam("gameId") String gameId){
		
		GameModel accountModel = gameBean.getGameStats(gameId);
		ServiceResult<GameModel> result = new ServiceResult<GameModel>();
		result.setStatus(ServiceResultStatus.successful);
		result.setContent(accountModel);
		
		return result;
	}
	
	@GET
	@Path("/latest")
	@Produces("application/json;charset=UTF-8")
	public ServiceResult<List<String>> getLatestGames(@Context UriInfo url,
			@PathParam("sessionId") String sessionId){
		
		List<String> games = gameBean.getLatestGames();
		ServiceResult<List<String>> result = new ServiceResult<List<String>>();
		result.setStatus(ServiceResultStatus.successful);
		result.setContent(games);
		
		return result;
	}
	
	@GET
	@Path("/{gameId}/tips")
	@Produces("application/json;charset=UTF-8")
	public ServiceResult<List<Tip>> getLatestTips(@Context UriInfo url,
			@PathParam("sessionId") String sessionId,
			@PathParam("gameId") String gameId){
		
		List<Tip> tips = gameBean.getLatestTips();
		ServiceResult<List<Tip>> result = new ServiceResult<List<Tip>>();
		result.setStatus(ServiceResultStatus.successful);
		result.setContent(tips);
		
		return result;
	}
}
