package ch.viascom.lusio.beans;

import javax.ejb.Stateless;
import javax.inject.Inject;

import ch.viascom.base.exceptions.ServiceException;

@Stateless
public class TipBean {
	
	@Inject
	TipDBBean tipDBBean;
	
	public String createTip(String tipId, String fieldId, String gameId, int amount,
			String userId) throws ServiceException {
		
		//New or Edit
		if(tipId != null && tipId.length() > 0){
			//Edit
			return "Edit";
		}else{
			//New
			return tipDBBean.createNewTip(tipId, fieldId, gameId, amount, userId);
		}
		
	}

	public boolean deleteTip(String tipId, String userId) {
		return false;
	}
}
