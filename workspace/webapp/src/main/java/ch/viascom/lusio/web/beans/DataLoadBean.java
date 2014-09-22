package ch.viascom.lusio.web.beans;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named("dataLoadBean")
@SessionScoped
public class DataLoadBean implements Serializable {

    private static final long serialVersionUID = 1L;

    
    @Inject
    private GamesBean gamesBean;
    
    public void initLoad(){
        gamesBean.loadGames();
    }
}
