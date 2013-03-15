package ch.viascom.lusio.beans;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import ch.viascom.lusio.module.AccountModel;
import ch.viascom.lusio.module.SessionModel;

@Stateless
public class AccountBean {
	
	public SessionModel login(String username,String password){
		
		
		
		return null;
	}
	
	public boolean logout(String sessionId){
		return false;
	}
	
	public AccountModel getAccountInformations(String sessionId){
		return null;
	}
}
