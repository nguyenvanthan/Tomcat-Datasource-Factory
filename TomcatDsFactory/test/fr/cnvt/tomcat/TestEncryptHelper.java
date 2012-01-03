package fr.cnvt.tomcat;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestEncryptHelper {
	
	@Test
	public void generateEncryptedValues(){
		String login = "monsupertest";
		String password = "monsupertestaussi";
		String cryptedLogin = EncryptHelper.encryptData(SecuredFactory.secretKeyUser, login);
		String cryptedPassword = EncryptHelper.encryptData(SecuredFactory.secretKeyPassword, password);
		String decryptedLogin = EncryptHelper.decryptData(SecuredFactory.secretKeyUser, cryptedLogin);
		String decryptedPassword = EncryptHelper.decryptData(SecuredFactory.secretKeyPassword, cryptedPassword);
		assertEquals(login, decryptedLogin); 
		assertEquals(password, decryptedPassword); 
		System.out.println("Login crypte : " + cryptedLogin);
		System.out.println("Mot de passe crypte : " + cryptedPassword);
	}

}
