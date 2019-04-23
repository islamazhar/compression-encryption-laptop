package utilities;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
/**
 *
 * @author javagists.com
 *
 */
public class STFP {

    public static void main(String[] args) throws Exception {

        JSch jsch = new JSch();
        Session session = null;
        try {
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");

            session = jsch.getSession("uiu", "103.109.52.10", 22);
            session.setPassword("cseuiu123@#");
            session.setConfig(config);
            session.connect(30000);

            Channel channel = session.openChannel("sftp");
            channel.connect();
            ChannelSftp sftpChannel = (ChannelSftp) channel;
            //sftpChannel.get("/home/uiu/mazharul/Lab/test.txt", "/Users/mazharul.islam/test.txt");
            long s = System.currentTimeMillis();
            sftpChannel.put("/Users/mazharul.islam/Dropbox/sizes/32768.txt", "/home/uiu/mazharul/Lab/test.txt");
            long e = System.currentTimeMillis();
            sftpChannel.exit();
            session.disconnect();
            System.out.println(e-s);
        } catch (JSchException e) {
            e.printStackTrace();
        } catch (SftpException e) {
            e.printStackTrace();
        }
    }
}
