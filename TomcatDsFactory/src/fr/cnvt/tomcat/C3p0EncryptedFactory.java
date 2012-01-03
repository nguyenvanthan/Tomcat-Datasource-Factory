package fr.cnvt.tomcat;

import java.beans.PropertyVetoException;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.RefAddr;
import javax.naming.Reference;

import com.mchange.v2.c3p0.ComboPooledDataSource;


/**
 * Datasource for C3P0
 * @author Kris
 *
 */
public class C3p0EncryptedFactory extends org.apache.naming.factory.BeanFactory implements SecuredFactory {

	public final static String ACQUIRE_INCREMENT                    = "acquireIncrement";
	public final static String ACQUIRE_RETRY_ATTEMPTS               = "acquireRetryAttempts";
	public final static String ACQUIRE_RETRY_DELAY                  = "acquireRetryDelay";
	public final static String AUTOMATIC_TEST_TABLE                 = "automaticTestTable";
	public final static String AUTO_COMMIT_ON_CLOSE                 = "autoCommitOnClose";
	public final static String BREAK_AFTER_ACQUIRE_FAILURE          = "breakAfterAcquireFailure";
	public final static String CHECKOUT_TIMEOUT                     = "checkoutTimeout";
	public final static String CONNECTION_TESTER_CLASS_NAME         = "connectionTesterClassName";
	public final static String IDLE_CONNECTION_TEST_PERIOD          = "idleConnectionTestPeriod";
    public final static String INITIAL_POOL_SIZE                    = "initialPoolSize"; 
    public final static String FACTORY_CLASS_LOCATION               = "factoryClassLocation";
    public final static String FORCE_IGNORE_UNRESOLVED_TRANSACTIONS = "forceIgnoreUnresolvedTransactions";
    public final static String MAX_POOL_SIZE                        = "maxPoolSize";
    public final static String MAX_IDLE_TIME                        = "maxIdleTime";
    public final static String MIN_POOL_SIZE                        = "minPoolSize";
    public final static String MAX_STATEMENTS                       = "maxStatements";
    public final static String MAX_STATEMENTS_PER_CONNECTION        = "maxStatementsPerConnection";
    public final static String NUM_HELPER_THREADS                   = "numHelperThreads";
    public final static String PREFERRED_TEST_QUERY                 = "preferredTestQuery";
    public final static String PROPERTY_CYCLE                       = "propertyCycle";
    public final static String TEST_CONNECTION_ON_CHECKOUT          = "testConnectionOnCheckout";
    public final static String TEST_CONNECTION_ON_CHECKIN           = "testConnectionOnCheckin";
    public final static String USES_TRADITIONAL_REFLECTIVE_PROXIES  = "usesTraditionalReflectiveProxies";

    public final static String USERNAME  = "user";
    public final static String PASSWORD  = "password";
    public final static String URL  = "jdbcUrl";
    public final static String DRIVER_CLASS  = "driverClass";

	@SuppressWarnings("rawtypes")
	@Override
	public Object getObjectInstance(Object obj, Name name, Context nameCtx, Hashtable environment) throws NamingException {
		ComboPooledDataSource cpds = new ComboPooledDataSource();
		Reference r = (Reference)obj;
		// recuperation de toutes les proprietes
		Enumeration<RefAddr> addrs = r.getAll();
		
		while (addrs.hasMoreElements()) {
	          RefAddr addr = addrs.nextElement();
	          String paramName = addr.getType();
	          String value = (String) addr.getContent();
	          
	          if (ACQUIRE_INCREMENT.equals(paramName)) {
	        	  cpds.setAcquireIncrement(Integer.parseInt(value));
	          } 
	          if (ACQUIRE_RETRY_ATTEMPTS.equals(paramName)) {
	        	  cpds.setAcquireRetryAttempts(Integer.parseInt(value));
	          } 
	          if (ACQUIRE_RETRY_DELAY.equals(paramName)) {
	        	  cpds.setAcquireRetryDelay(Integer.parseInt(value));
	          } 
	          if (CHECKOUT_TIMEOUT.equals(paramName)) {
	        	  cpds.setCheckoutTimeout(Integer.parseInt(value));
	          } 
	          if (IDLE_CONNECTION_TEST_PERIOD.equals(paramName)) {
	        	  cpds.setIdleConnectionTestPeriod(Integer.parseInt(value));
	          } 
	          if (INITIAL_POOL_SIZE.equals(paramName)) {
	        	  cpds.setInitialPoolSize(Integer.parseInt(value));
	          } 
	          if (MAX_POOL_SIZE.equals(paramName)) {
	        	  cpds.setMaxPoolSize(Integer.parseInt(value));
	          } 
	          if (MAX_IDLE_TIME.equals(paramName)) {
	        	  cpds.setMaxIdleTime(Integer.parseInt(value));
	          } 
	          if (MIN_POOL_SIZE.equals(paramName)) {
	        	  cpds.setMinPoolSize(Integer.parseInt(value));
	          } 
	          if (PREFERRED_TEST_QUERY.equals(paramName)) {
	        	  cpds.setPreferredTestQuery(value);
	          } 
	          if (TEST_CONNECTION_ON_CHECKIN.equals(paramName)) {
	        	  cpds.setTestConnectionOnCheckin(Boolean.parseBoolean(value));
	          } 
	          
	          if (USERNAME.equals(paramName)) {
					String pwd = EncryptHelper.decryptData(secretKeyUser, value.trim());
					cpds.setPassword(pwd);		
	          }
			  if (PASSWORD.equals(paramName)) {
					String username = EncryptHelper.decryptData(secretKeyPassword, value.trim());
					cpds.setUser(username);
			  }
			  if (URL.equals(paramName)) {
				  cpds.setJdbcUrl(value);
			  }
			  if (DRIVER_CLASS.equals(paramName)) {
				  try {
					cpds.setDriverClass(value);
				} catch (PropertyVetoException e) {
					e.printStackTrace();
				}
			  }
	      }
		
		System.out.println("Initialisation de la datasource JNDI : OK");
		
		return cpds;
	}

}
