/**
 * @author mazharul.islam
 *
 */
package encryption;



import compression.BinaryStdIn;
import compression.BinaryStdOut;

import java.io.*;
import java.util.*;
import java.security.*;
import java.security.spec.*;

import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;


import compression.CHT;
import file.transfer.DropboxClient;
import org.bouncycastle.jce.ECNamedCurveTable;
//import org.spongycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.spec.ECParameterSpec;
import utilities.Files;
//import org.spongycastle.jce.spec.ECParameterSpec;


public class ECC {

    /**
     * @param args
     */
    Cipher cipher = null;
    SecretKey key = null;
    int length = 0;
    public static int BLOCK_SIZE = 1024; //*1024;
    //ArrayList<Integer> numbers = null;
    //public List encryptedtSymbolsList = null;
    KeyPairGenerator kpg = null;
    PrivateKey privKey = null;
    PublicKey pubKey = null;
    public ECC() {
        // constructor
        try {
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            ECParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec("secp256k1");
            kpg = KeyPairGenerator.getInstance("ECDSA", "BC");
            kpg.initialize(ecSpec, new SecureRandom());
            KeyPair kpU = kpg.generateKeyPair();
            this.privKey = kpU.getPrivate();
            this.pubKey = kpU.getPublic();
            cipher = Cipher.getInstance("ECIES", "BC");
        }catch(Exception ex) {
            System.out.println("In constructor of ECC");
            System.out.println(ex.toString());
            ex.printStackTrace();
        }
    }


    public long decryption(String encryptedFileName, String decryptedFileName) {
        long tim1 = System.currentTimeMillis();
        BinaryStdIn.takeInputFile(encryptedFileName);
        BinaryStdOut.takeInputFile(decryptedFileName);
        int length = 0;
        try {
            cipher.init(Cipher.DECRYPT_MODE, privKey, new SecureRandom());
            while((length = BinaryStdIn.readInt())!=-1){
                byte [] cipherText = new byte[length];
                for(int i=0;i<cipherText.length;i++) {
                    cipherText[i]  = BinaryStdIn.readByte();
                }
                ECC ecc = new ECC();
                byte[] tempPlainText = ecc.doDecryption(cipher,cipherText);
                for(int i=0;i<tempPlainText.length;i++){
                    BinaryStdOut.write(tempPlainText[i]);
                }
            }

        }catch(Exception ex) {
            System.out.println("Decryption huffman tree!");
            System.out.println(ex.toString());
        }
        BinaryStdOut.close();
        BinaryStdIn.close();
        long tim2 = System.currentTimeMillis();
        return tim2 - tim1;
    }

    public  byte[] doEncryption(Cipher cipher, byte[] plainText ){
        try {
            //cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] tempCipherText = cipher.doFinal(plainText);
            return tempCipherText;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    public  byte[] doDecryption(Cipher cipher, byte[] cipherText ){
        try {
            byte[] tempCipherText = cipher.doFinal(cipherText);
            return tempCipherText;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    public  long encryption (String plainTextFile, String encryptedTextFile) {
        long tim1 = System.currentTimeMillis();
        BinaryStdIn.takeInputFile(plainTextFile);
        BinaryStdOut.takeInputFile(encryptedTextFile);
        byte [] plainText = BinaryStdIn.readAllBytes();
        //System.out.println(plainText.length);
        try {
            //System.out.println(plainText.length);
            cipher.init(Cipher.ENCRYPT_MODE, pubKey, new SecureRandom());
            for(int i=0;i<plainText.length;i+=BLOCK_SIZE) {
                int s = i;
                int e = Math.min(s + BLOCK_SIZE, plainText.length);
                //System.out.println(s+" - "+ e+" "+(e-s+1));
                byte[] tempPlainText = Arrays.copyOfRange(plainText, s, e);
                //System.out.println(new String(tempPlainText));
                ECC ecc = new ECC();
                byte[] tempCipherText = ecc.doEncryption(cipher,tempPlainText);
                BinaryStdOut.write(tempCipherText.length);
                //System.out.println(tempCipherText.length);
                //System.out.println("Writing");
                for(int j=0;j<tempCipherText.length;j++){
                    BinaryStdOut.write(tempCipherText[j]);
                }
            }
            BinaryStdOut.write(-1);



        }catch(Exception ex) {
            System.out.println("Encrypting huffman tree!");
            System.out.println(ex.toString());
        }
        BinaryStdOut.close();
        BinaryStdIn.close();
        long tim2 = System.currentTimeMillis();
        return tim2 - tim1;
    }

    ///*
    public static void main(String[] args) throws  IOException{
      //  String filetype = "songs";
      //  String filetype = "office";
      //  String filetype = "video";
        String filetype = "ebooks";
        DropboxClient dropboxClient = new DropboxClient();
        String dropboxDirectory = "/upload/";

        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File("results-laptop-dropbox.csv"), true));
        String directoryAddress = "/Users/mazharul.islam/Documents/format-corpus/"+filetype+"/";
        String outputDirectoryAddress = "/Users/mazharul.islam/Documents/format-corpus/"+filetype+"/Full/";
        Files files = new Files(directoryAddress);
        List<String> filesName = files.getFiles();

        for(String fileName: filesName) {
            try {

                String source = directoryAddress + fileName;
                System.out.println(source);
                String encryptedFileName = outputDirectoryAddress + fileName + ".en";
                String decryptedFileName = outputDirectoryAddress + fileName + ".de";
                for (int i = 0; i < 1; i++) {
                    ECC ecc = new ECC();
                    double tim1 = ecc.encryption(source, encryptedFileName);
                    double uploadTime = dropboxClient.HTTPUP(encryptedFileName, dropboxDirectory + fileName);
                    double downloadTime = dropboxClient.HTTPUP(encryptedFileName, dropboxDirectory + fileName);
                    double tim2 = ecc.decryption(encryptedFileName, decryptedFileName);
                    double size = new File(source).length() / 1000.00;
                    String line = fileName + " (" + Math.round(size) + "KB)," + "Full Encryption and decryption," + tim1 + "," + new File(source).length() + ",ECC encryption," + filetype+"\n";
                    bos.write(line.getBytes());
                    System.out.print(line);
                    line = fileName + " (" + Math.round(size) + "KB)," + "Full Encryption and decryption," + tim2 + "," + new File(source).length() + ",ECC decryption," + filetype+"\n";
                    bos.write(line.getBytes());
                    System.out.print(line);
                    line = fileName + " (" + Math.round(size) + "KB)," + "Full Encryption and decryption-CS," + new File(encryptedFileName).length() + "," + new File(source).length() + ",ECC scale," + filetype+"\n";
                    bos.write(line.getBytes());
                    System.out.print(line);
                    line = fileName + " (" + Math.round(size) + "KB)," + "Full Encryption and decryption," + uploadTime + "," + new File(source).length() + ",ECC upload," + filetype+"\n";
                    bos.write(line.getBytes());
                    System.out.print(line);
                    line = fileName + " (" + Math.round(size) + "KB)," + "Full Encryption and decryption," + downloadTime + "," + new File(source).length() + ",ECC download," + filetype+"\n";
                    bos.write(line.getBytes());
                    System.out.print(line);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        bos.close();
    }
}
	
/*
class Entity{
	public PrivateKey privKey = null;
	public PublicKey pubKey = null;
	public  Entity() {
			KeyPairGenerator kpg;
			try {
				kpg = KeyPairGenerator.getInstance("EC","SunEC");
				ECGenParameterSpec ecsp = new ECGenParameterSpec("secp256r1");
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
*/