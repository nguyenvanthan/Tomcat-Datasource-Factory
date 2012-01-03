package fr.cnvt.tomcat;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.Name;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;
import org.apache.tomcat.dbcp.dbcp.BasicDataSourceFactory;

/**
 * Datasource for Basic DBCP
 * @author Kris
 *
 */
public class BasicDBCPEncryptedFactory extends BasicDataSourceFactory implements SecuredFactory{


	@Override
	public Object getObjectInstance(Object obj, Name name, Context nameCtx, Hashtable environment) throws Exception {
		Object o = super.getObjectInstance(obj, name, nameCtx, environment);
		if (o != null && o instanceof BasicDataSource) {
			BasicDataSource ds = (BasicDataSource) o;
			String cryptedUsername = ds.getUsername();
			String cryptedPassword = ds.getPassword();
			if (cryptedPassword != null && cryptedPassword.length() > 0) {
				String pwd = EncryptHelper.decryptData(secretKeyPassword, cryptedPassword);
				ds.setPassword(pwd);
			}
			if (cryptedUsername != null && cryptedUsername.length() > 0) {
				String username = EncryptHelper.decryptData(secretKeyUser, cryptedUsername);
				ds.setUsername(username);
			}
			System.out.println("Initialisation de la datasource JNDI : OK");
			return ds;
		} else {
			System.out.println("Initialisation de la datasource JNDI : KO");
			return null;
		}
	}

}
