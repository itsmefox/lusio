package ch.viascom.lusio;

import javax.annotation.ManagedBean;
import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import ch.viascom.lusio.base.exceptions.ServiceResult;
import ch.viascom.lusio.base.exceptions.ServiceResultStatus;
import ch.viascom.lusio.beans.AccountBean;
import ch.viascom.lusio.module.AccountModel;

@ManagedBean
@Path("{sessionId}")
public class Account {

	@EJB
	AccountBean account;
	
	@GET
	@Path("/logout")
	@Produces("application/json;charset=UTF-8")
	public ServiceResult<Boolean> logout(@Context UriInfo url,
			@PathParam("sessionId") String sessionId){
		
		Boolean success = account.logout(sessionId);
		ServiceResult<Boolean> result = new ServiceResult<Boolean>();
		result.setStatus((success) ? ServiceResultStatus.successful : ServiceResultStatus.failed);
		result.setContent(null);
		
		return result;
	}
	
	@GET
	@Path("/account/info")
	@Produces("application/json;charset=UTF-8")
	public ServiceResult<AccountModel> getAccountInformations(@Context UriInfo url,
			@PathParam("sessionId") String sessionId){
		
		AccountModel accountModel = account.getAccountInformations(sessionId);
		ServiceResult<AccountModel> result = new ServiceResult<AccountModel>();
		result.setStatus(ServiceResultStatus.successful);
		result.setContent(accountModel);

		return result;
	}
	
}
