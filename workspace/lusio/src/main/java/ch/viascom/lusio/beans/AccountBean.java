package ch.viascom.lusio.beans;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import ch.viascom.lusio.module.AccountModel;
import ch.viascom.lusio.module.SessionModel;
import ch.viascom.lusio.util.LangUtil;
import ch.viascom.lusio.util.Security;

@Stateless
public class AccountBean {
	
	public SessionModel login(final String username,final String password){
		
		String dbUsername = "testUser";
		String dbPassword = "geheimnis";
		
		Boolean sessionSuccessfullCreated = false;
		SessionModel session = null;
		
		if (LangUtil.saveEquals(dbUsername, username) && LangUtil.saveEquals(dbPassword, Security.MD5(password))) {
			// Create Session
			
			
			sessionSuccessfullCreated = true;
			// 
		}
		
		
		
		return session;
	}
	
	public boolean logout(String sessionId){
		
		Boolean sessionSuccessfullDeleted = false;
		
		
		
		return sessionSuccessfullDeleted;
	}
	
	public AccountModel getAccountInformations(String sessionId){
		return null;
	}
}
