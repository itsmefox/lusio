package ch.viascom.lusio;

import javax.enterprise.context.SessionScoped;

import lombok.Data;

import ch.viascom.lusio.business.ServiceProviderConfig;
import java.io.Serializable;

@Data
@SessionScoped
public class DataStore implements Serializable {
    private static final long serialVersionUID = 1L;
    private ServiceProviderConfig config;
    private String sessionId;
}
