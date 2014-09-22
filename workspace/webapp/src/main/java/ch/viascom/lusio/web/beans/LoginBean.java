package ch.viascom.lusio.web.beans;

import java.io.IOException;
import java.util.logging.Logger;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.context.RequestContext;

import ch.viascom.base.utilities.URIUtil;
import ch.viascom.lusio.App;
import ch.viascom.lusio.DataStore;
import ch.viascom.lusio.business.DataServiceProvider;
import ch.viascom.lusio.business.ServiceProviderConfig;
import ch.viascom.lusio.web.model.LoginUser;

import lombok.Getter;

@Named("loginBean")
@SessionScoped
public class LoginBean extends BaseBean {

    private final Logger _logger = Logger.getLogger(LoginBean.class.getName());

    private static final long serialVersionUID = 1L;

    private static final String LOGIN_PAGE = "login.xhtml";
    private static final String INDEX_PAGE = "index.xhtml";

    @Getter
    private LoginUser loggedInUser = null;

    // CDI laedt aus dem aktuellen RequestScope !!
    @Inject
    private LoginUser loginUser;

    @Inject
    private DataStore store;
    
    @Inject
    private DataServiceProvider serviceProvider;
    
    @Inject
    private DataLoadBean loadBean;

    public void login(ActionEvent actionEvent) {

        initUser(null);
        
        RequestContext context = RequestContext.getCurrentInstance();
        FacesContext MessageContext = FacesContext.getCurrentInstance();  

        try {
            String sessionId = serviceProvider.login(loginUser.getUsername(), loginUser.getPassword());
            _logger.info(sessionId);
            store.setSessionId(sessionId);
            loggedInUser = loginUser;
            initUser(sessionId);
            redirectToPage(INDEX_PAGE);
        } catch (Exception e) {
            MessageContext.addMessage(null, new FacesMessage("Login fehlgeschlagen", "Falscher Benutzername oder Passwort"));  
            context.addCallbackParam("loggedIn", false);
        }

    }

    public void checkLoginState() {
        if (loggedInUser == null) {
            redirectToPage(LOGIN_PAGE);
        }else{
            loadBean.initLoad();
        }
    }

    public void logout() {
        _logger.info("Logout request");
        try {
            if (serviceProvider.logout()) {
                loggedInUser = null;
                redirectToPage(LOGIN_PAGE);
            }
        } catch (Exception e) {
            // TODO: Fehler senden
        }

    }

    private void redirectToPage(String page) {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(page);
        } catch (IOException e) {
        }
    }

    private void initUser(String sessionId) {
        String rootUrl = "https://lusio.viascom.ch/";
        if (sessionId != null) {
            rootUrl = URIUtil.concat("https://lusio.viascom.ch/", sessionId);
        }

        ServiceProviderConfig config;
        config = new ServiceProviderConfig();
        config.setSessionId(sessionId);
        config.setRequestRootURL(rootUrl);
        config.setRequestEncoding("UTF-8");
        config.setResponseEncoding("UTF-8");

        store.setConfig(config);
        
        _logger.info("Session id: " + config.getSessionId());
        _logger.info("Root URL: " + config.getRequestRootURL());
        _logger.info("Request encoding: " + config.getRequestEncoding());
        _logger.info("Response encoding: " + config.getResponseEncoding());
    }
}
