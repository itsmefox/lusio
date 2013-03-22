package ch.viascom.lusio.beans;

import javax.ejb.Stateless;

@Stateless
public class TipBean {

	public String createTip(String tipId, String fieldId, double amount,
			String userId) {
		return null;
	}

	public boolean deleteTip(String tipId,String userId) {
		return false;
	}
}
