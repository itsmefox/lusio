package ch.viascom.lusio;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import ch.viascom.base.exceptions.ServiceException;
import ch.viascom.base.exceptions.ServiceResult;
import ch.viascom.base.exceptions.ServiceResultStatus;
import ch.viascom.lusio.beans.AccountBean;
import ch.viascom.lusio.beans.TipBean;
import ch.viascom.lusio.interceptor.IsAuthorized;
import ch.viascom.lusio.module.AccountModel;

@ManagedBean
@Path("{sessionId}/tip")
public class Tips {

	@Inject
	TipBean tipBean;

	@Inject
	AccountBean account;

	@IsAuthorized
	@GET
	@Path("/create")
	@Produces("application/json;charset=UTF-8")
	public ServiceResult<String> createTip(@Context UriInfo url,
			@Context HttpServletRequest hsr,
			@PathParam("sessionId") String sessionId,
			@QueryParam("tipid") String tipId,
			@QueryParam("fieldid") String fieldId,
			@QueryParam("gameid") String gameId,
			@QueryParam("amount") int amount) throws ServiceException {

		String newTipId = null;

		AccountModel user = account.getAccountInformations(sessionId);
		System.out.println(tipId);
		newTipId = tipBean.createTip(tipId, fieldId, gameId, amount, user.getId());

		ServiceResult<String> result = new ServiceResult<String>();
		result.setStatus((newTipId != null) ? ServiceResultStatus.successful
				: ServiceResultStatus.failed);
		result.setContent(newTipId);

		return result;

	}

	@IsAuthorized
	@GET
	@Path("{tipId}/remove")
	@Produces("application/json;charset=UTF-8")
	public ServiceResult<String> deleteTip(@Context UriInfo url,
			@Context HttpServletRequest hsr,
			@PathParam("sessionId") String sessionId,
			@PathParam("tipId") String tipId) throws ServiceException {

		Boolean success = false;

		AccountModel user = account.getAccountInformations(sessionId);
		success = tipBean.deleteTip(tipId, user.getId());

		ServiceResult<String> result = new ServiceResult<String>();
		result.setStatus((success) ? ServiceResultStatus.successful
				: ServiceResultStatus.failed);
		result.setContent(null);

		return result;

	}
}
