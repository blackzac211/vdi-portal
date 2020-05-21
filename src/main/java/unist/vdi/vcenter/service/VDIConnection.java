package unist.vdi.vcenter.service;

import java.security.KeyStore;

import com.vmware.vapi.bindings.StubConfiguration;
import com.vmware.vapi.protocol.HttpConfiguration;
import com.vmware.vapi.protocol.HttpConfiguration.KeyStoreConfig;
import com.vmware.vapi.protocol.HttpConfiguration.SslConfiguration;

import unist.vdi.common.CommonUtil;
import vmware.samples.common.SslUtil;
import vmware.samples.common.authentication.VapiAuthenticationHelper;
import vmware.samples.common.authentication.VimAuthenticationHelper;

public class VDIConnection {
	private final String server = "vdi-vc.unist.ac.kr";
	private final String username = "administrator@vsphere.local";
	private final String password = "Un!s7i!vc";
	private final boolean skipServerVerification = true;
	
	private String truststorePath;
	private String truststorePassword;
	private VimAuthenticationHelper vimAuthHelper;
	private VapiAuthenticationHelper vapiAuthHelper;
	private StubConfiguration sessionStubConfig;
	
	private HttpConfiguration httpConfig;
	
	private static VDIConnection instance;
	
	
	/*
	private static class InnerInstance {
		private static final VDIConnection instance = new VDIConnection();
	}
	*/
	
	synchronized public static void initInstance() {
		if(instance != null) {
			instance.logout();
		}
		instance = new VDIConnection();
		instance.login();
	}
	
	synchronized public static VDIConnection getInstance() {
		if(instance == null) {
			instance = new VDIConnection();
			instance.login();
		}
		return instance;
	}
	
	/**
	 * Logs out of the server
	 * 
	 * @throws Exception
	 */
	private void logout() {
		try {
			vapiAuthHelper.logout();
			vimAuthHelper.logout();
			instance = null;
		} catch(Exception e) {
			CommonUtil.writeErrorLogs("logout exception: " + e.getMessage());
		}
	}
	
	/**
	 * Creates a session with the server using username/password.
	 *
	 * <p>
	 * <b> Note: If the "skip-server-verification" option is specified, then this
	 * method trusts the SSL certificate from the server and doesn't verify it.
	 * Circumventing SSL trust in this manner is unsafe and should not be used with
	 * production code. This is ONLY FOR THE PURPOSE OF DEVELOPMENT ENVIRONMENT <b>
	 * </p>
	 * 
	 * @throws Exception
	 */
	private void login() {
		try {
			vapiAuthHelper = new VapiAuthenticationHelper();
			vimAuthHelper = new VimAuthenticationHelper();
			httpConfig = buildHttpConfiguration();
			
			sessionStubConfig = vapiAuthHelper.loginByUsernameAndPassword(server, username, password, httpConfig);
			vimAuthHelper.loginByUsernameAndPassword(server, username, password);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Builds the Http settings to be applied for the connection to the server.
	 * 
	 * @return http configuration
	 * @throws Exception
	 */
	private HttpConfiguration buildHttpConfiguration() throws Exception {
		HttpConfiguration httpConfig = new HttpConfiguration.Builder().setSslConfiguration(buildSslConfiguration()).getConfig();

		return httpConfig;
	}

	/**
	 * Builds the SSL configuration to be applied for the connection to the server
	 * 
	 * For vApi connections: If "skip-server-verification" is specified, then the
	 * server certificate verification is skipped. The method retrieves the
	 * certificate from specified server and adds it to an in-memory trustStore
	 * which is returned. If "skip-server-verification" is not specified, then it
	 * uses the truststorepath and truststorepassword to load the truststore and
	 * return it.
	 *
	 * For VIM connections: If "skip-server-verification" is specified, then it
	 * trusts all the VIM API connections made to the specified server. If
	 * "skip-server-verification" is not specified, then it sets the System
	 * environment property "javax.net.ssl.trustStore" to the path of the file
	 * containing the trusted server certificates.
	 *
	 * <p>
	 * <b> Note: Below code circumvents SSL trust if "skip-server-verification" is
	 * specified. Circumventing SSL trust is unsafe and should not be used in
	 * production software. It is ONLY FOR THE PURPOSE OF DEVELOPMENT ENVIRONMENTS.
	 * <b>
	 * </p>
	 * 
	 * @return SSL configuration
	 * @throws Exception
	 */
	protected SslConfiguration buildSslConfiguration() throws Exception {
		SslConfiguration sslConfig;

		if (skipServerVerification) {
			/*
			 * Below method enables all VIM API connections to the server without validating
			 * the server certificates.
			 *
			 * Note: Below code is to be used ONLY IN DEVELOPMENT ENVIRONMENTS.
			 * Circumventing SSL trust is unsafe and should not be used in production
			 * software.
			 */
			SslUtil.trustAllHttpsCertificates();

			/*
			 * Below code enables all vAPI connections to the server without validating the
			 * server certificates..
			 *
			 * Note: Below code is to be used ONLY IN DEVELOPMENT ENVIRONMENTS.
			 * Circumventing SSL trust is unsafe and should not be used in production
			 * software.
			 */
			sslConfig = new SslConfiguration.Builder().disableCertificateValidation().disableHostnameVerification().getConfig();
		} else {
			/*
			 * Set the system property "javax.net.ssl.trustStore" to the truststorePath
			 */
			System.setProperty("javax.net.ssl.trustStore", truststorePath);
			KeyStore trustStore = SslUtil.loadTrustStore(truststorePath, truststorePassword);
			KeyStoreConfig keyStoreConfig = new KeyStoreConfig("", truststorePassword);
			sslConfig = new SslConfiguration.Builder().setKeyStore(trustStore).setKeyStoreConfig(keyStoreConfig).getConfig();
		}

		return sslConfig;
	}
	
	public VapiAuthenticationHelper getVapiAuthHelper() {
		return vapiAuthHelper;
	}
	public StubConfiguration getSessionStubConfig() {
		return sessionStubConfig;
	}
	
	public VimAuthenticationHelper getVimAuthHelper() {
		return vimAuthHelper;
	}
}
