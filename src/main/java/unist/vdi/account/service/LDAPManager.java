package unist.vdi.account.service;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;


public class LDAPManager {
	private Hashtable<String, String> env = new Hashtable<String, String>();
	private DirContext ctx = null;
	private final String[] BASE_DN = {"ou=울산과학기술원,dc=unist,dc=ac,dc=kr",
										"ou=특별법인 울산과학기술원,dc=unist,dc=ac,dc=kr",
										"ou=외부인력,dc=unist,dc=ac,dc=kr"};
	
	public LDAPManager() {
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		// env.put(Context.PROVIDER_URL, "ldaps://dc01.unist.ac.kr:636");
		env.put(Context.PROVIDER_URL, "ldap://dc01.unist.ac.kr");
		// env.put(Context.SECURITY_PROTOCOL, "ssl");
		// env.put(Context.SECURITY_AUTHENTICATION, "simple");
		// env.put(Context.SECURITY_PRINCIPAL, "CN=Administrator,CN=Users,DC=unist,DC=ac,DC=kr");
	}
	
	public void authenticateAsAdmin() {
		env.put(Context.SECURITY_PRINCIPAL, "vdi");
		env.put(Context.SECURITY_CREDENTIALS, "Un!s7##vdi");
		try {
			ctx = new InitialDirContext(env);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean authenticate(String user, String pwd) throws Exception {
		env.put(Context.SECURITY_PRINCIPAL, "unist\\" + user);
		env.put(Context.SECURITY_CREDENTIALS, pwd);
		ctx = new InitialDirContext(env);
		if(ctx != null) {
			return true;
		}
		return false;
	}
	
	public Attributes searchUser(String attribute, String value) {
		try {
			String filter = "(&(objectClass=user)(" + attribute + "=" + value + "))";
			
			SearchControls sc = new SearchControls();
			sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
			SearchResult rs = null;
			
			for(int i = 0; i < BASE_DN.length; i++) {
				NamingEnumeration<SearchResult> results = ctx.search(BASE_DN[i], filter, sc);
				for(int j = 0; results.hasMore(); j++) {
					if(j > 0) {
						throw new Exception("Matched multiple users for the " + attribute + ": " + value);
					}
					rs = (SearchResult)results.next();
					if(rs != null)
						break;
				}
			}
			Attributes attrs = rs.getAttributes();
			return attrs;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void closeContext() {
		try {
			ctx.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
