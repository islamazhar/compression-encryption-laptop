package file.transfer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPSClient;

import ourmethod.Huffman;

public class Client {
	final public static String USERNAME = "mazharul-islam";
	final public static String PASS = "r";

	
	/*
	 * FTPS FILE UP
	 */
	
	public long FTPSUP(String source, String des) {
		FTPSClient client = new FTPSClient();
		client.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
		long tim1 = 0;
		long tim2 = 0;
		try {
			
			String srcFilename = source;
			PrintWriter pw = new PrintWriter("FTSP_size_time_aes.txt");
		//	long avg = 0;
			String filename = des;
			File file = new File(srcFilename);
			client.connect("192.168.0.104",21);
			client.login(USERNAME, PASS);
			client.execPBSZ(0);
			client.execPROT("P");
			client.setFileTransferMode(FTP.BINARY_FILE_TYPE);
			client.enterLocalPassiveMode();
			client.setFileType(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
			FileInputStream srcFileStream = new FileInputStream(file);
			tim1 = System.currentTimeMillis();
			boolean status = client.storeFile(filename, srcFileStream);
			tim2 = System.currentTimeMillis();
			srcFileStream.close();
			client.disconnect();
			pw.close();
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return tim2-tim1;
		
	}
	/*
	 * FTPS FILE DOWNLOAD
	 */
	public long FTPSDownload(String source, String des) {
		FTPSClient client = new FTPSClient();
		client.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
		long tim1 = 0;
		long tim2 = 0;
		try {
			
			String srcFilename = source;
			PrintWriter pw = new PrintWriter("FTSP_size_time_aes.txt");
			long avg = 0;
			String filename = des;
			File file = new File(srcFilename);
			client.connect("192.168.0.104",21);
			client.login(USERNAME, PASS);
			client.execPBSZ(0);
			client.execPROT("P");
			client.setFileTransferMode(FTP.BINARY_FILE_TYPE);
			client.enterLocalPassiveMode();
			client.setFileType(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
			FileOutputStream srcFileStream = new FileOutputStream(file);
			tim1 = System.currentTimeMillis();
			boolean status = client.retrieveFile(filename, srcFileStream);
			tim2 = System.currentTimeMillis();
			srcFileStream.close();
			client.disconnect();
			pw.close();
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return tim2-tim1; 
	}
	
	/*
	 * FTP FILE UPLOAD
	 */
	public long FTPUP(String source, String des) {
		FTPClient client = new FTPClient();
		client.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
		long tim1 = 0;
		long tim2 = 0;
		try {
			
			String srcFilename = source;
			PrintWriter pw = new PrintWriter("FTSP_size_time_aes.txt");
			long avg = 0;
			String filename = des;
			File file = new File(srcFilename);
			client.connect("192.168.0.104",21);
			client.login(USERNAME, PASS);
			
			client.setFileTransferMode(FTP.BINARY_FILE_TYPE);
			client.enterLocalPassiveMode();
			client.setFileType(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
			FileInputStream srcFileStream = new FileInputStream(file);
			tim1 = System.currentTimeMillis();
			boolean status = client.storeFile(filename, srcFileStream);
			tim2 = System.currentTimeMillis();
			srcFileStream.close();
			client.disconnect();
			pw.close();
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return tim2-tim1;
	}
	
	/*
	 * FTP FILE DOWNLOAD
	 */
	
	public long FTPDownload (String source, String des) {
		FTPClient client = new FTPClient();
		client.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
		long tim1 = 0;
		long tim2 = 0;
		try {
			
			String srcFilename = source;
			PrintWriter pw = new PrintWriter("FTSP_size_time_aes.txt");
			long avg = 0;
			String filename = des;
			File file = new File(srcFilename);
			client.connect("192.168.0.104",21);
			client.login(USERNAME, PASS);
			client.setFileTransferMode(FTP.BINARY_FILE_TYPE);
			client.enterLocalPassiveMode();
			client.setFileType(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
			FileInputStream srcFileStream = new FileInputStream(file);
			tim1 = System.currentTimeMillis();
			boolean status = client.storeFile(filename, srcFileStream);
			tim2 = System.currentTimeMillis();
			srcFileStream.close();
			client.disconnect();
			pw.close();
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return tim2-tim1;
	}
	
	public static void main(String args[]) {

		Client client = new Client();
		String localFile = "/Users/mazharul.islam/Desktop/R2.txt";
		String localZipFile = "/Users/mazharul.islam/Desktop/R2.zip";
		String remoteFile = "/home/mazharul-islam/512/hello.txt";
		String remoteZipFile = "/home/mazharul-islam/512/hello.zip";
		//long t1 = client.FTPSUP("/Users/mazharul.islam/Desktop/R2.txt", "/home/mazharul-islam/512/hello.txt");
	//	client.FTPSDownload("/Users/mazharul.islam/Desktop/R3.txt", "/home/mazharul-islam/512/hello.txt");
		
		long t = 0;
	//	Huffman huffman = new Huffman();
	//	t = huffman.compress(localFile,localZipFile);
		t += 	client.FTPSUP(localZipFile,remoteZipFile);
		System.out.println("Time is " + t);
		
	}
}

/***
 * 
 * sudo gedit /etc/vsftpd.conf
 * sudo systemctl restart vsftpd
 * 
 * ******/


