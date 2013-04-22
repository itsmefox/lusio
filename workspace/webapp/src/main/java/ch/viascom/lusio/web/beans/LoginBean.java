package ch.viascom.lusio.web.beans;

import java.io.IOException;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.context.RequestContext;

import ch.viascom.lusio.web.model.LoginUser;

import lombok.Getter;

@Named("loginBean")
@SessionScoped
public class LoginBean extends BaseBean{
    
    private static final long serialVersionUID = 1L;

    private static final String LOGIN_PAGE = "login.xhtml";
    private static final String INDEX_PAGE = "index.xhtml";
    
    @Getter
    private LoginUser loggedInUser = null;
    
    //CDI laedt aus dem aktuellen RequestScope !!
    @Inject
    private LoginUser loginUser; 
  
    public void login(ActionEvent actionEvent) {    
       
        RequestContext context = RequestContext.getCurrentInstance();
          
        if("admin".equals(loginUser.getUsername()) && "admin".equals(loginUser.getPassword())) {  
            loggedInUser = loginUser;   
            redirectToPage(INDEX_PAGE);
        } else {  
            context.addCallbackParam("loggedIn", false);  
        }  
          
       
       
    } 
    
    public void checkLoginState(){
        if(loggedInUser == null){
            redirectToPage(LOGIN_PAGE);
        }
    }
    
    public void logout(){
        loggedInUser = null;
        redirectToPage(LOGIN_PAGE);
    }

    private void redirectToPage(String page) {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(page);
        } catch (IOException e) {}
    }
}
