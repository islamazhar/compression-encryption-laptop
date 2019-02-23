/**
 * @author mazharul.islam
 *
 */
package encryption;



import java.util.ArrayList;
import java.util.List;
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
	
	public List<Integer> encryptionBitWise(ECC ecc, List<Integer> plainText){
		String planTextString = "";
		
		for(Integer num:plainText) {
			if(num == 1) {
				planTextString = planTextString + '1';
			}
			else {
				planTextString = planTextString + '0';
			}
		}
		
		byte [] planTextBytes = planTextString.getBytes();
		byte [] encryptedTextBytes = ecc.encryption(planTextBytes);
	//	for(byte bit:encryptedTextBytes) {
	//		System.out.print(bit+" ");
	//	}
	//	System.out.println("");
		List<Integer> ret = new ArrayList<Integer>();
		List<Integer> temp = new ArrayList<Integer>();
		for(int i=0;i<encryptedTextBytes.length;i++) {
			int num = encryptedTextBytes[i];
			temp.clear();
			for(int j=0;j<8;j++){ // 8 bar cholbe
				if(( num & 1) ==1) {
					temp.add(1);
				}
				else {
					temp.add(0);
				}
				num = num >> 1;
			}
			for(int j=temp.size()-1;j>=0;j--) {
				ret.add(temp.get(j));
			}
		}
		return ret;
	}
	
	public List<Integer> decryptionBitWise(ECC ecc, List<Integer> encrypted){
		
		List<Integer> t =new ArrayList<Integer>();
		int pos=0;
		int bytes=0;
		for(int i=0;i<encrypted.size();i++) {
			bytes = bytes << 1; 
			if(encrypted.get(i)==1) {
				bytes = bytes | 1;
			}
			pos++;
			if(pos==8) {
				t.add(bytes);
				bytes = 0 ;
				pos = 0;
			}
		}
		byte [] encryptedBytes = new byte[t.size()];
		
		for(int i=0;i<t.size();i++) {
			encryptedBytes[i] = (byte)(int)t.get(i);
		}
	//	for(byte bit:encryptedBytes) {
	//		System.out.print(bit+" ");
	//	}
		byte [] decryptedBytes = ecc.decryption(encryptedBytes);
		String decryptedText = new String(decryptedBytes);
		List<Integer> ret = new ArrayList<Integer>();
		for(int i=0;i<decryptedText.length();i++) {
			if(decryptedText.charAt(i)=='1') {
				ret.add(1);
			}
			else {
				ret.add(0);
			}
		}
		return ret;
	}
	

	public static void main(String[] args) throws Exception {
		/*
		Entity eccU = new Entity();
		Entity eccV = new Entity();
		//	PrintWriter out2 = new PrintWriter("ECC_time.txt");
		PrintWriter out = new PrintWriter("ECC_cf.txt");
		String folderlocation = "/home/mazharul-islam/files";
		long startTime,totalTime;

		// generate symmetric key
		try {
			String huffmanTreeFile = "_huffman_tree.txt";
			String huffmanTreeEncryptedFile = "_huffman_tree_encrypted.txt";
			String compressedFile = "_compressed.txt";
			Cipher cipher;



			KeyAgreement ecdhU = KeyAgreement.getInstance("ECDH"); // Elliptic Curve Diffie Hellman
			ecdhU.init(eccU.privKey);
			ecdhU.doPhase(eccV.pubKey,true); // V is the receiver
			byte [] symmetricKey = ecdhU.generateSecret();
			//	System.out.println("Length :" +symmetricKey.length);
			SecretKey key = new SecretKeySpec(symmetricKey, 0, symmetricKey.length, "AES");
			//	System.out.println(key.toString());
			cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			//	System.out.println(key.toString().length());

			//	System.out.println("provider=" + cipher.getProvider());
			//	String cleartextFile = "huffmanTree.txt";
			//	String ciphertextFile = "huffmanTreeEncryptedECC.txt";
			byte[] block = new byte[64];


			cipher.init(Cipher.ENCRYPT_MODE, key);
			FileInputStream fis = new FileInputStream(huffmanTreeFile);
			FileOutputStream fos = new FileOutputStream(huffmanTreeEncryptedFile);
			CipherOutputStream cos = new CipherOutputStream(fos, cipher);
			int i;
			// encryption
			while ((i = fis.read(block)) != -1) {
				startTime =  System.nanoTime();
				cos.write(block, 0, i);
				//totalTime   += System.nanoTime()-startTime;
			}
			cos.close();
			fis.close();
			fos.close();

			String cleartextAgainFile = "huffmanTreeDecrpted.txt";
			cipher.init(Cipher.DECRYPT_MODE,key);
			fis = new FileInputStream(huffmanTreeEncryptedFile);
			CipherInputStream cis = new CipherInputStream(fis, cipher);
			fos = new FileOutputStream(cleartextAgainFile);
			while ((i = cis.read(block)) != -1) {
				fos.write(block, 0, i);
			}
			fos.close();
			cis.close();

			//	if(flag == 1) out2.print(" ");
			//	out2.print(totalTime);


			File file1 = new File(huffmanTreeEncryptedFile);
			File file2 = new File(compressedFile);
			File file3 = new File(huffmanTreeFile);



			file1.delete();
			file2.delete();
			file3.delete();

			//out2.close();
		}catch(Exception ex) {

		}
		*/
		/*
		ECC ecc = new ECC();
		byte[] plainText = new String("Mazharul").getBytes();
		byte[] cipherText = ecc.encryption(plainText);
		byte[] decryptedTextBytes = ecc.decryption(cipherText);
		String decryptedText = new String(decryptedTextBytes);
		System.out.println(decryptedText);
		*/
		// bit level encryption checking
		
		ArrayList<Integer> test = new ArrayList<Integer>();
		ArrayList<Integer> plainIntegers = new ArrayList<Integer>();
	//	for(int i=1;i<24;i++) {
	//		test.add((i+1)%2);
	//	}
		test.add(1);
		test.add(1);
		test.add(0);
		test.add(1);
		
		test.add(1);
		test.add(1);
		test.add(1);
		test.add(1);
		
		test.add(1);
		test.add(1);
		test.add(0);
		test.add(1);
		System.out.println(test.toString());
		ECC ecc = new ECC();
		List<Integer> encrypted =  ecc.encryptionBitWise(ecc, test);
		System.out.println(encrypted.toString());
	//	System.out.println(encrypted.size());
		List<Integer> decrypted = ecc.decryptionBitWise(ecc, encrypted);
		System.out.println(decrypted.toString());
	}
	
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