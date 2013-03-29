package ch.viascom.lusio.beans;

import javax.ejb.Stateless;
import javax.inject.Inject;

import ch.viascom.base.exceptions.ServiceException;

@Stateless
public class TipBean {

    @Inject
    TipDBBean tipDBBean;

    public String createTip(String fieldId, String gameId, int amount, String userId) throws ServiceException {
        return tipDBBean.createNewTip(fieldId, gameId, amount, userId);
    }

    public boolean deleteTip(String tipId, String userId) throws ServiceException {
        return tipDBBean.removeTip(tipId, userId);
    }
}
