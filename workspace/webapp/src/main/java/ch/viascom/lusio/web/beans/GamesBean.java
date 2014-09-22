package ch.viascom.lusio.web.beans;

import java.util.ArrayList;
import java.util.logging.Logger;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.component.menuitem.MenuItem;
import org.primefaces.component.submenu.Submenu;
import org.primefaces.model.DefaultMenuModel;
import org.primefaces.model.MenuModel;

import ch.viascom.lusio.business.DataServiceProvider;
import ch.viascom.lusio.module.GameModel;
import java.io.Serializable;

@Named("gamesBean")
@SessionScoped
public class GamesBean implements Serializable {

    private final Logger _logger = Logger.getLogger(DataServiceProvider.class.getName());
    
    private static final long serialVersionUID = 1L;

    private ArrayList<GameModel> gameModels = new ArrayList<>();

    public ArrayList<GameModel> getGameModels() {
        _logger.info("Zugriffe auf "+gameModels.size()+" Spiele");
        return gameModels;
    }
   
    @Inject
    DataServiceProvider serviceProvider;
    

    public void loadGames() {
        _logger.info("loading Games");
        try {
            //gameModels.addAll(serviceProvider.getLatestGames());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        GameModel gameModel = new GameModel();
        gameModel.setId("1234");
        
        gameModels.add(gameModel);
    }

}
