package ch.viascom.lusio;

import javax.annotation.ManagedBean;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import ch.viascom.lusio.base.exceptions.ServiceResult;
import ch.viascom.lusio.base.exceptions.ServiceResultStatus;
import ch.viascom.lusio.beans.AccountBean;
import ch.viascom.lusio.beans.TipBean;
import ch.viascom.lusio.module.AccountModel;

@ManagedBean
@Path("{sessionId}/tip")
public class Tip {

	
	@EJB
	TipBean tipBean;
	
	@EJB
	AccountBean accountBean;
	
	@POST
	@Path("/create")
	@Consumes("application/x-www-form-urlencoded")
	@Produces("application/json;charset=UTF-8")
	public ServiceResult<String> createTip(@Context UriInfo url,
			@PathParam("sessionId") String sessionId,
			@FormParam("tipId") String tipId,
			@FormParam("fieldId") String fieldId,
			@FormParam("amount") double amount) {

		AccountModel user = accountBean.getAccountInformations(sessionId);
		
		String newTipId = tipBean.createTip(tipId, fieldId, amount,user.getId());
		ServiceResult<String> result = new ServiceResult<String>();
		result.setStatus(ServiceResultStatus.successful);
		result.setContent(newTipId);

		return result;

	}
	
	@GET
	@Path("{tipId}/remove")
	@Produces("application/json;charset=UTF-8")
	public ServiceResult<String> deleteTip(@Context UriInfo url,
			@PathParam("sessionId") String sessionId,
			@PathParam("tipId") String tipId) {

		AccountModel user = accountBean.getAccountInformations(sessionId);
		
		Boolean success = tipBean.deleteTip(tipId, user.getId());
		ServiceResult<String> result = new ServiceResult<String>();
		result.setStatus((success) ? ServiceResultStatus.successful : ServiceResultStatus.failed);
		result.setContent(null);

		return result;

	}
}
