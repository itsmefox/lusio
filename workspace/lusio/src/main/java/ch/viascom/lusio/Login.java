package ch.viascom.lusio;

import javax.annotation.ManagedBean;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.FormParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import ch.viascom.lusio.base.exceptions.ServiceResultStatus;
import ch.viascom.lusio.base.exceptions.ServiceResult;
import ch.viascom.lusio.beans.AccountBean;
import ch.viascom.lusio.module.SessionModel;

@ManagedBean
@Path("")
public class Login {

	@EJB
	AccountBean account;

	@POST
	@Path("/login")
	@Produces("application/json;charset=UTF-8")
	public ServiceResult<SessionModel> login(@Context UriInfo url,
			@FormParam("username") String username,
			@FormParam("password") String password) {

		SessionModel session = account.login(username, password);
		ServiceResult<SessionModel> result = new ServiceResult<SessionModel>();
		result.setStatus(ServiceResultStatus.successful);
		result.setContent(session);

		return result;

	}
}