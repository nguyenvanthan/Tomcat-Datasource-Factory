package fr.cnvt.tomcat;

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.Name;

/**
 * Datasource for Tomcat 7 JDBC pool
 * @author Kris
 *
 */
public class Tomcat7EncryptedFactory extends org.apache.tomcat.jdbc.pool.DataSourceFactory implements SecuredFactory{


	@SuppressWarnings("rawtypes")
	@Override
	public Object getObjectInstance(Object obj, Name name, Context nameCtx, Hashtable environment) throws Exception {
		Object o = super.getObjectInstance(obj, name, nameCtx, environment);
		if (o != null && o instanceof org.apache.tomcat.jdbc.pool.DataSource) {
			org.apache.tomcat.jdbc.pool.DataSource ds = (org.apache.tomcat.jdbc.pool.DataSource) o;
			// recuperation du login et mot de passe cryptes
			String cryptedUsername = ds.getUsername();
			String cryptedPassword = ds.getDbProperties().getProperty("password");
			// on decrypte le password
			if (cryptedPassword != null && cryptedPassword.length() > 0) {
				String pwd = EncryptHelper.decryptData(secretKeyPassword, cryptedPassword);
				ds.setPassword(pwd);
			}
			// on decrypte le login
			if (cryptedUsername != null && cryptedUsername.length() > 0) {
				String username = EncryptHelper.decryptData(secretKeyUser, cryptedUsername);
				ds.setUsername(username);
			}
			// on set la taille initiale avec le minimum idle
			// on doit laisser le initialSize a 0 car il ne faut pas cree de connexion tout de suite car on a les identifiants cryptes.
			int minIdleSize = ds.getMinIdle();
			ds.setInitialSize(minIdleSize);
			ds.setConnectionProperties(ds.getConnectionProperties());
			
			// on ferme le pool deja parametre
			ds.close();
			// on recree le pool avec les nouveaux parametres
			ds.createPool();
			System.out.println("Initialisation de la datasource JNDI : OK");
			return ds;
		} else {
			System.out.println("Initialisation de la datasource JNDI : KO");
			return null;
		}
	}

}
