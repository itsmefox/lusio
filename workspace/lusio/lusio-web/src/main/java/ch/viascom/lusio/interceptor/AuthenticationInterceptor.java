package ch.viascom.lusio.interceptor;

import java.lang.reflect.AnnotatedElement;
import java.util.List;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.servlet.http.HttpServletRequest;

import ch.viascom.base.exceptions.ServiceException;
import ch.viascom.lusio.beans.AccountBean;
import ch.viascom.lusio.entity.User;

@IsAuthorized
@Interceptor
public class AuthenticationInterceptor {

    @Inject
    AccountBean account;

    @AroundInvoke
    public Object manage(InvocationContext ctx) throws Exception {

        Object[] parameters = ctx.getParameters();

        HttpServletRequest hsr = (HttpServletRequest) parameters[1];
        String sessionId = (String) parameters[2];

        if (account.isAuthorized(sessionId, hsr.getHeader("X-Forwarded-For"))) {
            return ctx.proceed();
        } else {
            throw new ServiceException("NOT_AUTHORIZED", "").setResponseStatusCode(500).addRequestParameter("sessionId", sessionId);
        }

    }


}
