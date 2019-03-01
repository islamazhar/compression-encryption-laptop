/**
 * @author mazharul.islam
 *
 */
package encryption;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.security.*;
import java.security.spec.*;

import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class ECC {
	
	/**
	 * @param args
	 */
	Entity eccU = null;
	Entity eccV = null;
	Cipher cipher = null;
	SecretKey key = null;
	int length = 0;
	//public List encryptedtSymbolsList = null;
	public ECC() {
		// constructor
		try {
			eccU = new Entity();
			eccV = new Entity();
			KeyAgreement ecdhU = KeyAgreement.getInstance("ECDH"); // Elliptic Curve Diffie Hellman
			ecdhU.init(eccU.privKey);
			ecdhU.init(eccU.privKey);
			ecdhU.doPhase(eccV.pubKey,true); // V is the receiver
			byte [] symmetricKey = ecdhU.generateSecret();
			key = new SecretKeySpec(symmetricKey, 0, symmetricKey.length, "AES");
			cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		}catch(Exception ex) {
			System.out.println("In constructor of ECC");
			System.out.println(ex.toString());
		}
	}
	

	public byte[] decryption(byte[] cipherText) {
		try {
			//System.out.println(cipherText.length);
			cipher.init(Cipher.DECRYPT_MODE,key);
			byte[] res = cipher.doFinal(cipherText);
			return res;
		}catch(Exception ex) {
			System.out.println("Decrypting huffman tree!");
			System.out.println(ex.toString());
		}
		return null;
	}
	
	public Map<Integer, Integer>decryptionMap(byte[] cipherText) {
		Map<Integer, Integer> Decrypted= null;
		try {
			//System.out.println(cipherText.length);
			cipher.init(Cipher.DECRYPT_MODE,key);
			byte[] res = cipher.doFinal(cipherText);
			
			String str = new String(res);
			//System.out.println(str);
			str = str.substring(1, str.length()-1); 
			String[] keyValuePairs = str.split(","); 
			Decrypted = new HashMap();
			for(String pair : keyValuePairs)                        //iterate over the pairs
			{
			    String[] entry = pair.split("=");                   //split the pairs to get key and value 
			    Decrypted.put(Integer.valueOf(entry[0].trim()), Integer.valueOf(entry[1].trim()));          
			    //add them to the hashmap and trim whitespaces
			}
			//System.out.println(Decrypted.toString());
			
		}catch(Exception ex) {
			System.out.println("Decrypting huffman tree!");
			System.out.println(ex.toString());
		}
		return Decrypted;
	}
	
	public  byte[] encryption (byte[] plainText) {
		try {
			//System.out.println(plainText.length);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] res = cipher.doFinal(plainText);
			length = res.length;
			return res;
			
		}catch(Exception ex) {
			System.out.println("Encrypting huffman tree!");
			System.out.println(ex.toString());
		}
		return null;
	
	}
	

	//public static void main(String[] args) throws Exception {		
//	}
	
}

class Entity{
	public PrivateKey privKey = null;
	public PublicKey pubKey = null;
	public  Entity() {
			KeyPairGenerator kpg;
			try {
				kpg = KeyPairGenerator.getInstance("EC","SunEC");
				ECGenParameterSpec ecsp = new ECGenParameterSpec("secp192r1"); 
				kpg.initialize(ecsp);
				KeyPair kpU = kpg.genKeyPair();
				this.privKey = kpU.getPrivate();
				this.pubKey = kpU.getPublic();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	}
}