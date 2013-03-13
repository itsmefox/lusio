package ch.viascom.lusio.jersey.exceptions;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import ch.viascom.lusio.base.exceptions.ServiceException;
import ch.viascom.lusio.base.exceptions.ServiceFault;
import ch.viascom.lusio.base.exceptions.ServiceResult;
import ch.viascom.lusio.base.exceptions.ServiceResultStatus;


/**
 * @author      : Viascom GmbH
 * @version     : 1.0
 * @create:     : 13.03.2013
 * @product     : Viascom-Base
 *
 * @email       : info@viascom.ch
 * @website     : http://viascom.ch
 */
@Provider
public class ExceptionMapper implements javax.ws.rs.ext.ExceptionMapper<Throwable> {
	private final Logger _logger = Logger.getLogger(ExceptionMapper.class.getName());
	
	
	public Response toResponse(Throwable ex) {
		
		if (ex instanceof ServiceException) {
			ServiceException serviceEx = (ServiceException)ex;
			
			// 404 not really an error
			if (serviceEx.getFault().getResponseStatusCode() == 404)
				_logger.log(Level.FINE, "Service-Fehler aufgetreten.", serviceEx);
			else
				_logger.log(Level.WARNING, "Service-Fehler aufgetreten.", serviceEx);
			
			ServiceResult<ServiceFault> result = new ServiceResult<ServiceFault>();
			result.setStatus(ServiceResultStatus.failed);
			result.setContent(serviceEx.getFault());

			return Response.status(serviceEx.getFault().getResponseStatusCode())
					.entity(result)
					.header("X-Status", "error")
					.type("application/json;charset=UTF-8")
					.build();
		}
		else {
			_logger.log(Level.SEVERE, "Unerwarteter Fehler aufgetreten.", ex);
			
			ServiceException serviceEx = new ServiceException("UNKNOWN", ex.getLocalizedMessage());
			serviceEx.setException(ex);
			
			ServiceResult<ServiceFault> result = new ServiceResult<ServiceFault>();
			result.setStatus(ServiceResultStatus.failed);
			result.setContent(serviceEx.getFault());
	
			return Response
					.status(serviceEx.getFault().getResponseStatusCode())
					.entity(result)
					.header("X-Status", "error")
					.type("application/json;charset=UTF-8")
					.build();
		}
	}
}