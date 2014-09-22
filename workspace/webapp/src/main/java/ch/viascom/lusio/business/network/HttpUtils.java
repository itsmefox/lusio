package ch.viascom.lusio.business.network;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpParams;

public class HttpUtils {
	private static final X509TrustManager trustManager = new TrustViascomX509TrustManager();
	
	
	/**
	 * Returns a new HttpClient implementation based
	 * on the given one which accepts all certificates
	 * as valid.
	 * Needed because of "No peer certificate".
	 */
	public static HttpClient wrapClient(HttpClient client) {
		try {
			SSLContext context = SSLContext.getInstance("TLS");
			context.init(null, new TrustManager[] { trustManager }, null);
			
			SSLSocketFactory sslSocketFactory = new CustomSSLSocketFactory(context);
			sslSocketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			
			ClientConnectionManager ccm = client.getConnectionManager();
			
			SchemeRegistry schemeRegistry = ccm.getSchemeRegistry();
			schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
			schemeRegistry.register(new Scheme("https", sslSocketFactory, 443));
			
			return new DefaultHttpClient(ccm, client.getParams());

			// ssf = new SSLSocketFactory(ctx);
			// ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		} catch (Exception ex) {
			throw new RuntimeException("Failed to create HTTPS client.", ex);
		}
	}
	
	/**
	 * Creates a thread safe HTTP client which reuses connections,
	 * allows multiple connections and trusts all certificates.
	 */
	public static HttpClient getThreadSafeTrustAllCertsClient() {
		HttpClient client = getThreadSafeClient();
		client = wrapClient(client);
		
		return client;
	}
	
	/**
	 * Creates a thread safe HTTP client which reuses connections,
	 * allows multiple connections.
	 */
	public static HttpClient getThreadSafeClient() {
		HttpClient client = new DefaultHttpClient();
		ClientConnectionManager mgr = client.getConnectionManager();
		HttpParams params = client.getParams();
		
		return new DefaultHttpClient(new ThreadSafeClientConnManager(params, mgr.getSchemeRegistry()), params);
	}
}
