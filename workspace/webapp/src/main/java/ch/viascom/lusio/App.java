package ch.viascom.lusio;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import javax.ejb.Singleton;
import javax.ejb.Startup;

import lombok.Getter;
import lombok.Setter;

import ch.viascom.base.utilities.URIUtil;
import ch.viascom.lusio.business.ServiceProviderConfig;

@Startup
@Singleton
public class App {
    private final Logger _logger = Logger.getLogger(App.class.getName());
    
    @PostConstruct
    public void onStartup() {
        _logger.info("---------- BEGIN onStartup lusio-webapp ----------");

        
        _logger.info("---------- END onStartup lusio-webapp ----------");
    }

    @PreDestroy
    public void onDestroy() {
        _logger.info("onDestroy lusio-webapp");
    }
}