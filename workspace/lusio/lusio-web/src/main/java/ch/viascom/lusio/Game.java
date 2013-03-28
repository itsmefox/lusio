package ch.viascom.lusio;

import java.util.List;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import ch.viascom.base.exceptions.ServiceException;
import ch.viascom.base.exceptions.ServiceResult;
import ch.viascom.base.exceptions.ServiceResultStatus;
import ch.viascom.lusio.beans.AccountBean;
import ch.viascom.lusio.beans.GameBean;
import ch.viascom.lusio.interceptor.IsAuthorized;
import ch.viascom.lusio.module.GameModel;
import ch.viascom.lusio.module.TipModel;

@ManagedBean
@Path("{sessionId}/game")
public class Game {

	@Inject
	GameBean gameBean;

	@Inject
	AccountBean account;

	@IsAuthorized
	@GET
	@Path("/{gameId}/stats")
	@Produces("application/json;charset=UTF-8")
	public ServiceResult<GameModel> getGameStats(@Context UriInfo url,
			@Context HttpServletRequest hsr,
			@PathParam("sessionId") String sessionId,
			@PathParam("gameId") String gameId) throws ServiceException {

		GameModel gameModel = null;

		gameModel = gameBean.getGameStats(gameId);

		ServiceResult<GameModel> result = new ServiceResult<GameModel>();
		result.setStatus(ServiceResultStatus.successful);
		result.setContent(gameModel);

		return result;
	}

	@IsAuthorized
	@GET
	@Path("/latest")
	@Produces("application/json;charset=UTF-8")
	public ServiceResult<List<GameModel>> getLatestGames(@Context UriInfo url,
			@Context HttpServletRequest hsr,
			@PathParam("sessionId") String sessionId) throws ServiceException {

		List<GameModel> games = null;

		games = gameBean.getLatestGames();

		ServiceResult<List<GameModel>> result = new ServiceResult<List<GameModel>>();
		result.setStatus(ServiceResultStatus.successful);
		result.setContent(games);

		return result;
	}

	@IsAuthorized
	@GET
	@Path("/{gameId}/tips")
	@Produces("application/json;charset=UTF-8")
	public ServiceResult<List<TipModel>> getLatestTips(@Context UriInfo url,
			@Context HttpServletRequest hsr,
			@PathParam("sessionId") String sessionId,
			@PathParam("gameId") String gameId) throws ServiceException {

		List<TipModel> tip = null;

		tip = gameBean.getLatestTips(gameId);

		ServiceResult<List<TipModel>> result = new ServiceResult<List<TipModel>>();
		result.setStatus(ServiceResultStatus.successful);
		result.setContent(tip);

		return result;
	}
}
