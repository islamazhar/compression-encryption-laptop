package file.transfer;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.users.FullAccount;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;


public class DropboxClient {
    private static final String ACCESS_TOKEN = "MsW4mTV8XWAAAAAAAAAPYIDVhCoMOfwjnZdoWMJfRb02eD76hHiczOQjXsfY7HX3";
    DbxClientV2 client = null;
    public DropboxClient() {
        DbxRequestConfig config = new DbxRequestConfig("dropbox/java-tutorial", "en_US");
        client = new DbxClientV2(config, ACCESS_TOKEN);
    }
    public long HTTPUP(String source, String des) {


        try {
            long t1 = System.currentTimeMillis();
            InputStream in = new FileInputStream(source);
            FileMetadata response = client.files().uploadBuilder(des)
                    .withAutorename(true)
                    .withMute(true)
                    .withClientModified(new Date())
                    .uploadAndFinish(in);
            // Now delete the file
            in.close();
            long t2 = System.currentTimeMillis();
            client.files().deleteV2(des);
            return t2 - t1;
        } catch ( Exception ex){
            ex.printStackTrace();
        }

        return 0;
    }
    public long HTTPDN(String source, String des) {
        long t1= System.currentTimeMillis();
        try {
            // Upload the file
            InputStream in = new FileInputStream(source);
            FileMetadata metadata = client.files().uploadBuilder(des)
                    .uploadAndFinish(in);
            in.close();
            //  downaload the file
            long time1 = System.currentTimeMillis();
            OutputStream os = new FileOutputStream(source);
            metadata = client.files().downloadBuilder(des).download(os);
            os.close();
            long time2 = System.currentTimeMillis();
            // delete the file
            client.files().deleteV2(des);
            return time2 - time1;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        long t2= System.currentTimeMillis();
        return t2 - t1;
    }
     public static void main(String args[]) {
         String folderName = "/Users/mazharul.islam/compression/HComECC/test-data/sizes/";

         for(int i=1024;i<=10240;i+=(10240 - 1024)) {
             String sourceFilename = folderName + i +".sh";
             String desFilename = "/upload/" + i;
             double t = 0;
             for(int j=0;j<20;j++){
                 DropboxClient dropboxClient = new DropboxClient();
                 t += dropboxClient.HTTPDN(sourceFilename, desFilename);
             }
             System.out.println(i+" "+t/20.00);
         }
     }
}
