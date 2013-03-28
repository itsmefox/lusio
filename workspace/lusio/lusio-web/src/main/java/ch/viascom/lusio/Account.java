package ch.viascom.lusio;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
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
import ch.viascom.lusio.interceptor.IsAuthorized;
import ch.viascom.lusio.module.AccountModel;
import ch.viascom.lusio.module.SessionModel;

@ManagedBean
@Path("/")
public class Account {

	@Inject
	AccountBean account;

	@IsAuthorized
	@GET
	@Path("{sessionId}/account/logout")
	@Produces("application/json;charset=UTF-8")
	public ServiceResult<String> logout(@Context UriInfo url,
			@Context HttpServletRequest hsr,
			@PathParam("sessionId") String sessionId) {

		Boolean success = false;

		success = account.logout(sessionId);

		ServiceResult<String> result = new ServiceResult<String>();
		result.setStatus((success) ? ServiceResultStatus.successful
				: ServiceResultStatus.failed);
		result.setContent(null);

		return result;
	}

	@IsAuthorized
	@GET
	@Path("{sessionId}/account/info")
	@Produces("application/json;charset=UTF-8")
	public ServiceResult<AccountModel> getAccountInformations(
			@Context UriInfo url, @Context HttpServletRequest hsr,
			@PathParam("sessionId") String sessionId) throws ServiceException {

		AccountModel accountModel = null;

		accountModel = account.getAccountInformations(sessionId);
		accountModel.setCredit(account.getCredit(accountModel.getId()));
		
		ServiceResult<AccountModel> result = new ServiceResult<AccountModel>();
		result.setStatus((accountModel != null) ? ServiceResultStatus.successful
				: ServiceResultStatus.failed);
		result.setContent(accountModel);

		return result;
	}

	@GET
	@Path("account/generateSession")
	@Consumes("application/x-www-form-urlencoded")
	@Produces("application/json;charset=UTF-8")
	public ServiceResult<SessionModel> login(@Context UriInfo url,
			@Context HttpServletRequest hsr,
			@QueryParam("username") String username,
			@QueryParam("password") String password) throws ServiceException {

		SessionModel session = account.login(username, password,
				hsr.getHeader("X-Forwarded-For"));
		ServiceResult<SessionModel> result = new ServiceResult<SessionModel>();
		result.setStatus(ServiceResultStatus.successful);
		result.setContent(session);

		return result;
	}

}
