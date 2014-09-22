package ch.viascom.lusio.business.network;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

/**
 * Trust Manager which trusts all certificates.
 */
public class TrustViascomX509TrustManager implements X509TrustManager {

	/**
	 * Checks if the client is trusted. Only for a server application needed
	 * to establish a connection to the client.
	 */
    public void checkClientTrusted(X509Certificate[] certificates, String authType) throws CertificateException {
    	// Don't check anything
    }
    
	/**
	 * Checks if the server is trusted. Only for a client application needed
	 * to establish a connection to the server.
	 */
    public void checkServerTrusted(X509Certificate[] certificates, String authType) throws CertificateException {
    	// Don't check anything
    }
    
	/**
	 * Return an array of certificate authority (CA) certificates
	 * which are trusted for authenticating peers.
	 * 
	 * null suppresses the authority validation.
	 */
    public X509Certificate[] getAcceptedIssuers() {
    	// Don't check anything
    	return null; 
    }
}
