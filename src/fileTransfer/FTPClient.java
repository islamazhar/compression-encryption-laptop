package fileTransfer;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPSClient;

public class FTPClient {
	public static String   DATA_FOLDER = "/home/mazharul-islam";
	public static void main(String args[]) {
		FTPSClient client = new FTPSClient();
		client.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
		try {
			
		//	FTPFile[] result = client.listFiles(DATA_FOLDER);
		//	for(int i=0;i<result.length;i++) {
		//		System.out.println(result[i].toString());
	//		}
			long tim1,tim2;
			boolean blank = false;
			String foldername = "/Users/mazharul.islam/Dropbox/Research/compression and encryption/Main paper/fig/compression-encryption/Graphs/Wired/test/";
			PrintWriter pw = new PrintWriter("FTSP_size_time_aes.txt");
			long avg = 0;
			//185128
			for(int i=128;i<=100000;i+=1000){
				String filename = foldername+String.valueOf(i)+".txt";
				File file = new File(filename);
				System.out.println(i);
				for(int j=0;j<10;j++) {
					client.connect("192.168.0.102",21);
					client.login("mazharul-islam", "r");
					client.execPBSZ(0);
					client.execPROT("P");
					client.enterLocalPassiveMode();
					client.setFileType(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
					FileInputStream srcFileStream = new FileInputStream(file);
					tim1 = System.currentTimeMillis();
					boolean status = client.storeFile("/home/mazharul-islam/512/Hello.txt", srcFileStream);
					tim2 = System.currentTimeMillis();
				//	if(!blank) {
				//		blank = true;
				//	}else {
				//		pw.print(" ");
				//	}
					srcFileStream.close();
					client.disconnect();
					avg+=tim2-tim1;
				}
				pw.print(file.length()+" ");
				pw.println(avg/10.00);
			}
			pw.close();
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
	}
}

