package utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class TextFileGenerate {
    int  fileSize;
    File folder = null;
    public ArrayList<String> fileNames = null ;
    public String folderName = null;

    public static void main(String args[]) {
        String folderlocation = "test-data/sizes/";
        File in = new File(folderlocation+"E.coli");
        File out = null;
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            System.out.println(in.length());
            for(Integer size = 860000;size<=in.length();size=size+860000) {
                //	for(Integer  size=1024;size<in.length() && size<=262144 ;size=size+1000) {
                fis = new FileInputStream(in);
                out = new File(folderlocation+String.valueOf(size)+".sh");
                fos = new FileOutputStream(out);

                int i;
                int read = 0;
                byte [] b = new byte[64];
                while( (i = fis.read(b)) !=-1)  {
                    //	while( (i = fis.read(b)) !=-1)  {
                    fos.write(b, 0, i);
                    read += i;
                    if(read>=size) {
                        break;
                    }
                }
                fos.close();
                fis.close();
            }
        }catch(Exception ex) {
            ex.printStackTrace();
        }
    }


}
