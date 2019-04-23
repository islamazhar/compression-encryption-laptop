/**
 * @author mazharul.islam
 *
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;


import com.javamex.classmexer.MemoryUtil;
import com.javamex.classmexer.MemoryUtil.VisibilityFilter;
import compression.Huffman;
import compression.RankOrderHuffman;
import encryption.ECC;
import file.transfer.DropboxClient;


public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        String folderName = "/Users/mazharul.islam/compression/HComECC/test-data/sizes/";
        Integer times = 20;
        DropboxClient client = new DropboxClient();

        String des= "/upload/";
        File sadRes = new File("/Users/mazharul.islam/Dropbox/upload-laptop.csv");
        PrintWriter pw = new PrintWriter(sadRes);

        pw.append("method,size,encom,time,direction\n");
/*
        for(Integer siz = 860000;siz<=4638690;siz=siz+860000)  {

            double tim = 0;
            double mem=0;

            String source = folderName+"/"+siz+".sh";
            Huffman huffman = new Huffman();

            for(int i=0;i<times;i++) {
                String inputFileNamme = folderName +siz+".sh";
                String compressedFile = source+".huffman";
                String enCompressedFile = source+".huffman.encrypted";
                String decryptedCompressedFile = compressedFile+".again";
                String outFile = source+".huffman.again.txt";


                double t1 = huffman.compress(inputFileNamme, compressedFile, enCompressedFile);
                double t2 = client.HTTPUP(enCompressedFile, des+siz+".zip"+i);
                pw.append("hcomecc,"+siz+","+t1+","+t2+",up\n");
                System.out.print("hcomecc,"+siz+","+t1+","+t2+",up\n");

               t2 = client.HTTPDN(enCompressedFile, des+siz+".zip"+i);
                t1 = huffman.deCompress(enCompressedFile, decryptedCompressedFile, outFile);
                pw.append("hcomecc,"+siz+","+t1+","+t2+",down\n");
                System.out.print("hcomecc,"+siz+","+t1+","+t2+",down\n");

                // tim +=t;
                // long usedMemo = MemoryUtil.memoryUsageOf(huffman);
                // long usedMemo = MemoryUtil.deepMemoryUsageOf(huffman, VisibilityFilter.ALL);
                // mem +=usedMemo;
            }
        }
        */
        /*
        for(Integer siz = 860000;siz<=4638690;siz=siz+860000)  {
            //String siz = "6144";
            String source = folderName+ siz+".sh";

            //double mem = 0;
            for(int i=0; i<times ; i++) {
                String compressedFile = source+".helios";
                String outFile = source+".helios.again.txt";

                RankOrderHuffman helios = new RankOrderHuffman();
                double t1 = helios.compress(source, compressedFile);
                double t2 = client.HTTPUP(compressedFile, des+siz+".zip"+i);
                pw.append("HEliOS,"+siz+","+t1+","+t2+",up\n");
                System.out.print("HEliOS,"+siz+","+t1+","+t2+",up\n");
                t2 = client.HTTPDN(compressedFile, des+siz+".zip"+i);
                t1 = helios.deCompress(compressedFile,outFile);

                pw.append("HEliOS,"+siz+","+t1+","+t2+",down\n");
               System.out.print("HEliOS,"+siz+","+t1+","+t2+",down\n");
                  //long usedMemo = MemoryUtil.memoryUsageOf(huffman);
                //  long usedMemo = MemoryUtil.deepMemoryUsageOf(huffman, VisibilityFilter.ALL);
                //  mem+=usedMemo;
            }
        }

        */


        for(Integer siz = 860000;siz<=4638690;siz=siz+860000)  {
            //String siz = "6144";
            String source = folderName+"/"+siz+".sh";
            String encryptedFile = source+".en";
            String decryptedFile = source+".de";
            String destination = "/upload/"+siz+".sh.server";
            //double mem = 0;
            for(int i=0; i<times ; i++) {
                ECC ecc = new ECC();
                double t1 = ecc.encryption(source, encryptedFile);
                double t2 = client.HTTPUP(encryptedFile, destination+siz);
                pw.append("FTPS,"+siz+","+t1+","+t2+",up\n");
                System.out.print("FTPS,"+siz+","+t1+","+t2+",up\n");
                t2 = client.HTTPDN(encryptedFile, destination+siz);
                t1 = ecc.decryption(encryptedFile, decryptedFile);
                pw.append("FTPS,"+siz+","+t1+","+t2+",down\n");
                System.out.print("FTPS,"+siz+","+t1+","+t2+",down\n");
            }
        }

        pw.close();

   }

}

/*
javac -cp .:commons-net-3.6.jar:classmexer.jar  Main.java
 java  -cp .:commons-net-3.6.jar:classmexer.jar -XX:-UseCompressedOops -javaagent:classmexer.jar Main
 */

/*****
 * Step1: compress the file
 * Step2: encrypt the HT and permutation Map
 * Step3: send the encrypt HT , permuation Map and compressed data
 */

/***
 * Send the data
 */

/***
 * Step1: decrypt the HT and permuation Map
 * Step: decode the compressed data
 */



