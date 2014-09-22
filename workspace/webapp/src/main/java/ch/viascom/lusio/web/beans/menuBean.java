package ch.viascom.lusio.web.beans;

import javax.annotation.ManagedBean;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;  
import javax.faces.context.FacesContext;  
import org.primefaces.component.menuitem.MenuItem;  
import org.primefaces.component.submenu.Submenu;  
import org.primefaces.model.DefaultMenuModel;  
import org.primefaces.model.MenuModel;
import java.io.Serializable;  
  
@ManagedBean
@SessionScoped
public class menuBean implements Serializable {  
  
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private MenuModel model;  
  
    public menuBean() {  
        model = new DefaultMenuModel();  
          
        //First submenu  
        Submenu submenu = new Submenu();  
        submenu.setLabel("Dynamic Submenu 1");  
          
        MenuItem item = new MenuItem();  
        item.setValue("Dynamic Menuitem 1.1");  
        item.setUrl("#");  
        submenu.getChildren().add(item);  
          
        model.addSubmenu(submenu);  
          
        //Second submenu  
        submenu = new Submenu();  
        submenu.setLabel("Dynamic Submenu 2");  
          
        item = new MenuItem();  
        item.setValue("Dynamic Menuitem 2.1");  
        item.setUrl("#");  
        submenu.getChildren().add(item);  
          
        item = new MenuItem();  
        item.setValue("Dynamic Menuitem 2.2");  
        item.setUrl("#");  
        submenu.getChildren().add(item);  
          
        model.addSubmenu(submenu);  
    }  
  
    public MenuModel getModel() {  
        
        return model;  
    }     
      
    public void save() {  
        addMessage("Data saved");  
    }  
      
    public void update() {  
        addMessage("Data updated");  
    }  
      
    public void delete() {  
        addMessage("Data deleted");  
    }  
      
    public void addMessage(String summary) {  
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary,  null);  
        FacesContext.getCurrentInstance().addMessage(null, message);  
    }  
}  