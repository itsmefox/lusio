package ch.viascom.lusio;

import javax.annotation.ManagedBean;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
import ch.viascom.lusio.module.AccountModel;
import ch.viascom.lusio.module.SessionModel;

@ManagedBean
@Path("/")
public class Account {

	//@EJB
	//AccountBean account;
	
//	@GET
//	@Path("{sessionId}/logout")
//	@Produces("application/json;charset=UTF-8")
//	public ServiceResult<Boolean> logout(@Context UriInfo url,
//			@PathParam("sessionId") String sessionId){
//		
//		Boolean success = account.logout(sessionId);
//		ServiceResult<Boolean> result = new ServiceResult<Boolean>();
//		result.setStatus((success) ? ServiceResultStatus.successful : ServiceResultStatus.failed);
//		result.setContent(null);
//		
//		return result;
//	}
//	
//	@GET
//	@Path("{sessionId}/account/info")
//	@Produces("application/json;charset=UTF-8")
//	public ServiceResult<AccountModel> getAccountInformations(@Context UriInfo url,
//			@PathParam("sessionId") String sessionId){
//		
//		AccountModel accountModel = account.getAccountInformations(sessionId);
//		ServiceResult<AccountModel> result = new ServiceResult<AccountModel>();
//		result.setStatus(ServiceResultStatus.successful);
//		result.setContent(accountModel);
//
//		return result;
//	}


	@GET
	@Path("account/generateSession")
	@Consumes("application/x-www-form-urlencoded")
	@Produces("application/json;charset=UTF-8")
	public ServiceResult<SessionModel> test(@Context UriInfo url
			,@QueryParam("username") String username
			,@QueryParam("password") String password
			){
		
		
		SessionModel session = new SessionModel();
		session.setSessionId("59221ec6-8c8d-492c-94ce-f3fdc2da7a47");
		
		
		//SessionModel session = account.login(username, password);
		ServiceResult<SessionModel> result = new ServiceResult<SessionModel>();
		result.setStatus(ServiceResultStatus.successful);
		result.setContent(session);

		return result;
	}
	

}
