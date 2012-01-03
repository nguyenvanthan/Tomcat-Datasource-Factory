package fr.cnvt.tomcat;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.apache.commons.codec.binary.Base64;

/**
 * Classe utilitaire de cryptage
 * 
 * @author cnguyenvanthan
 * 
 */
public final class EncryptHelper {

	// les algorithms dispo peut etre consulter dans l'appendix A de JCE (Java
	// Cryptography Extension)
	private static final String SECRET_KEY_ALGORITHM = "AES";
	private static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
	private static final String CHARSET_UTF8 = "UTF-8";
	private static final int KEY_SIZE = 128;

	private EncryptHelper() {
	}

	/**
	 * Encrypt une String suivant une cle passe dans le 1er parametre
	 * @param key la cle de cryptage
	 * @param encryptedString la chaine a crypter
	 */
	public static final String encryptData(String key, String data) {
		byte[] retour = null;
		String result = null;

		try {
			SecureRandom sr = new SecureRandom(key.getBytes("UTF-8"));
			byte[] entree = data.getBytes(CHARSET_UTF8);
			KeyGenerator kgen = KeyGenerator.getInstance(SECRET_KEY_ALGORITHM);
			kgen.init(KEY_SIZE, sr);
			SecretKey skey = kgen.generateKey();
			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, skey);
			retour = cipher.doFinal(entree);
			result = new String(Base64.encodeBase64(retour));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * Encrypt une String suivant une cle passe dans le 1er parametre
	 * @param key la cle de cryptage
	 * @param encryptedString la chaine a decrypter
	 */
	public static final String decryptData(String key, String encryptedString) {
		byte[] retour = null;
		String result = null;
		try {
			SecureRandom sr = new SecureRandom(key.getBytes("UTF-8"));
			byte[] entree = Base64.decodeBase64(encryptedString);
			KeyGenerator kgen = KeyGenerator.getInstance(SECRET_KEY_ALGORITHM);
			kgen.init(KEY_SIZE, sr);
			SecretKey skey = kgen.generateKey();
			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, skey);
			retour = cipher.doFinal(entree);
			result = new String(retour, CHARSET_UTF8);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}
	

}
