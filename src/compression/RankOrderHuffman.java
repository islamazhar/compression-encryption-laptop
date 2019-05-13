package compression;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import encryption.ECC;
import file.transfer.DropboxClient;
import utilities.Element;
import utilities.RedBlackBST;

import static java.lang.Math.cos;
import static java.lang.StrictMath.acos;

public class RankOrderHuffman {

    // chaotic pararms
    public static double d = 5;
    public static double w = 2.00;
    public static double z1 = 0.4;
    public static double z2 = 0.5;


    // alphabet size of extended ASCII
    private static final int R = 256;
    String filename = null;
    static String[] st = null;
    static Double[] stt = null;
    static Double[] stt2 = null;
    static Integer length = 0;
    //static Integer maxLevel= 25;
    static byte[] plainText = null;
    //static byte[] cipherText = null;
    static RedBlackBST<Element> ht = null;
    static RedBlackBST<Element> ht1 = null;
    static Map<Integer,String> Map1= null;
    static Map<String, Integer> Map2=null;
    static ECC ecc = null;
    // Do not instantiate.
    public RankOrderHuffman() {
        ecc = new ECC();
        ht = new RedBlackBST<>();
        ht1 = new RedBlackBST<>();
        Map1 = new HashMap<Integer,String>();
        Map2 = new HashMap<String, Integer>();
    }
    public  static double NthTerm (double w, double z, double d) {
        // w >= 2
        for(int i=0;i<d;i++) {
            z = cos(w*acos(z));
        }
        return z;
    }

    // Huffman trie node
    private static class Node implements Comparable<Node> {
        private final char ch;
        private final int freq;
        private final Node left, right;

        Node(char ch, int freq, Node left, Node right) {
            this.ch    = ch;
            this.freq  = freq;
            this.left  = left;
            this.right = right;
        }

        // is the node a leaf node?
        private boolean isLeaf() {
            assert ((left == null) && (right == null)) || ((left != null) && (right != null));
            return (left == null) && (right == null);
        }

        // compare, based on frequency
        public int compareTo(Node that) {
            return this.freq - that.freq;
        }
    }

    /**
     * Reads a sequence of 8-bit bytes from standard input; compresses them
     * using Huffman codes with an 8-bit alphabet; and writes the results
     * to standard output.
     */
    public static long compress(String filename) {
        // read the input
        String s = BinaryStdIn.readString();
        //System.out.println(s);
        char[] input = s.toCharArray();

        // tabulate frequency counts
        int[] freq = new int[R];
        for (int i = 0; i < input.length; i++) {
            freq[input[i]]++;
        }

        // build Huffman trie
        Node root = buildTrie(freq);

        // build code table

        st = new String[R];
        stt = new Double[R];
        stt2 = new Double[R];


        buildCode(st, root, "");


        writeTrie(root); // interesting place apply encryption on the root and then write.

        BinaryStdOut.close();
        BinaryStdOut.takeInputFile(filename);
        // print number of bytes in original uncompressed message

        BinaryStdOut.write(input.length);


			/*
			for(int i=0;i<plainText.length;i++) {
				System.out.print(plainText[i]);
			}
			System.out.println("");
			*/

        // System.out.println(ht.toString());
        // System.out.println("--------------------------------------");
        // System.out.println(ht1.toString());
        for (int i = 0; i < R; i++) {
            if(freq[i] != 0) {

                Element e1 = new Element(0, (char)i, st[i].length(),stt[(char)i]);

                int rank = ht.rank(e1);
              //  System.out.println(stt[i] + " "+ rank);
                Map1.put(rank,st[i]);

                Element e2 = new Element(0, (char)i, st[i].length(),stt2[i]);
                int rank2 = ht1.rank(e2);
             //   System.out.println(stt[i] + " "+ rank);
                Map2.put(st[i],rank2);
                if (rank2 != rank) {
                    //System.out.println(rank + " " + rank2);
                }

                freq[i] = 0;
            }
        }

        long ss = System.currentTimeMillis();

        for (int i = 0; i < input.length; i++) {

            String code = st[input[i]];
            //////////////////////////////////
            //changes should be here for rank order Huffman tree.
            Integer c = freq[input[i]];
            Integer len = code.length();
            Element e = new Element(c,input[i],len, stt[input[i]]);
            //System.out.println(h.toString());
            Integer pos = ht.rank(e);
            code = Map1.get(pos);
            //System.out.println(Map1.size()+" "+pos);
            ////////////////////////////////////////

            for (int j = 0; j < code.length(); j++) {
                if (code.charAt(j) == '0') {
                    BinaryStdOut.write(false);
                }
                else if (code.charAt(j) == '1') {
                    BinaryStdOut.write(true);
                }
                else throw new IllegalStateException("Illegal state");
            }
            ht.delete(e);
            e.curFrequency++;
            freq[input[i]]++;
            ht.put(e);
        }
        long e = System.currentTimeMillis();

        BinaryStdOut.close();
        return e-ss;
    }

    // build the Huffman trie given frequencies
    private static Node buildTrie(int[] freq) {

        // initialze priority queue with singleton trees
        MinPQ<Node> pq = new MinPQ<Node>();
        for (char i = 0; i < R; i++)
            if (freq[i] > 0)
                pq.insert(new Node(i, freq[i], null, null));

        // special case in case there is only one character with a nonzero frequency
        if (pq.size() == 1) {
            if (freq['\0'] == 0) pq.insert(new Node('\0', 0, null, null));
            else                 pq.insert(new Node('\1', 0, null, null));
        }

        // merge two smallest trees
        while (pq.size() > 1) {
            Node left  = pq.delMin();
            Node right = pq.delMin();
            Node parent = new Node('\0', left.freq + right.freq, left, right);
            pq.insert(parent);
        }
        return pq.delMin();
    }


    // write bitstring-encoded trie to standard output
    private static void writeTrie(Node x) {
        if (x.isLeaf()) {
            BinaryStdOut.write(true);
            BinaryStdOut.write(x.ch, 8);
            return;
        }
        BinaryStdOut.write(false);
        writeTrie(x.left);
        writeTrie(x.right);
    }

    // make a lookup table from symbols and their encodings
    private static void buildCode (String[] st, Node x, String s) {
        if (!x.isLeaf()) {
            buildCode(st, x.left,  s + '0');
            buildCode(st, x.right, s + '1');
        }
        else {
            st[x.ch] = s;
            // System.out.println(x.ch);
            ////////////////////////////////////
            ///extra for HEliOS
            ////////////////////////////////////
            Integer len = s.length();

            // now generate two different e1 and e2 and insert e1 in ht and e2 in ht2
            z1 = NthTerm(w, z1, d);
            z2 = NthTerm(w, z2, d);
            stt[x.ch] = z1;
            stt2[x.ch] = z2;
            Element e1  = new Element(0,x.ch,len,stt[x.ch]);
            Element e2  = new Element(0,x.ch,len,z2);

            ht.put(e1);
            ht1.put(e2); // for decoding
            //////////////////////////////
        }
    }

    /**
     * Reads a sequence of bits that represents a Huffman-compressed message from
     * standard input; expands them; and writes the results to standard output.
     */
    public static long expand(String filename) {


        long s = System.currentTimeMillis();
        // read in Huffman trie from input stream
        Node root = readTrie();
        BinaryStdIn.close();
        BinaryStdIn.takeInputFile(filename);

        // number of bytes to write
        int length = BinaryStdIn.readInt();


        // decode using the Huffman trie
        for (int i = 0; i < length; i++) {
            Node x = root;
            while (!x.isLeaf()) {
                boolean bit = BinaryStdIn.readBoolean();
                if (bit) x = x.right;
                else     x = x.left;
            }
            String code = st[x.ch];
            //Integer len = code.length();
            Integer c = Map2.get(code);
            Element e = ht1.select(c);
            BinaryStdOut.write(e.value, 8);
            ht1.delete(e);
            e.curFrequency++;
            ht1.put(e);
        }

        long e = System.currentTimeMillis();
        BinaryStdIn.close();
        BinaryStdOut.close();
        return e-s;
    }


    private static Node readTrie() {
        boolean isLeaf = BinaryStdIn.readBoolean();
        if (isLeaf) {
            return new Node(BinaryStdIn.readChar(), -1, null, null);
        }
        else {
            return new Node('\0', -1, readTrie(), readTrie());
        }
    }

    public double compress(String inputFileNamme, String compressed) {
        String tree = compressed+".tree";
        BinaryStdIn.takeInputFile(inputFileNamme);
        BinaryStdOut.takeInputFile(tree);
        double t = 0;
        t += compress(compressed);
        double treeEncryptionTime = ecc.encryption(tree,tree+".encrypted");
        t += treeEncryptionTime;
        return t;
    }

    public double deCompress (String compressed, String outputFile) {
        String tree = compressed+".tree";
        double decryptionTime = ecc.decryption(compressed+".tree.encrypted",tree);
        BinaryStdIn.takeInputFile(tree);
        BinaryStdOut.takeInputFile(outputFile);
        double t = expand(compressed);
        t += decryptionTime;
        return t;
    }
    public static  void main(String[] args)  throws  IOException{
        //String[] fileNames = {"emails", "ebooks", "office", "video", "audio"};
        String[] fileNames = {"emails"};
        try {
            DropboxClient dropboxClient = new DropboxClient();
            String dropboxDirectory = "/upload/";
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File("results-laptop-dropbox.csv"), true));

            for (String fileName : fileNames) {
                String directoryAddress = "/Users/mazharul.islam/Dropbox/upload-backup/";
                String outputDirectoryAddress = "/Users/mazharul.islam/Documents/format-corpus/" + fileName + "/HEliOS/";
                String source = directoryAddress + fileName;
                System.out.println(source);
                String compressedFile = outputDirectoryAddress + fileName + ".helios";
                String outFile = outputDirectoryAddress + fileName + ".helios.again.txt";
                for (int i = 0; i < 1; i++) {
                  //  System.out.println(source);
                    RankOrderHuffman helios = new RankOrderHuffman();
               //    /*
                    for(int j=0;j<1;j++) {
                        double compressionTime = helios.compress(source, compressedFile);
                    }
                //    */
                    /*
                    for(int j=0;j<20;j++) {
                        double uploadTime = dropboxClient.HTTPUP(compressedFile, dropboxDirectory + fileName+j);
                    }
                    */
                   /*
                    for(int j=0;j<20;j++) {
                        double downloadTime = dropboxClient.HTTPDN(compressedFile, dropboxDirectory + fileName+j);
                    }
                //    */

                //        /*
                    double decompressionTime = helios.deCompress(compressedFile, outFile); // download

                    /*
                    dropboxClient.delete(dropboxDirectory + fileName);

                    double size = new File(source).length() / 1000.00;
                    //System.out.println(size);
                    String line = fileName + " (" + Math.round(size) + "KB)," + "HEliOS-U," + compressionTime + "," + new File(source).length() + ",HEliOS compression," + fileName + "\n";
                    bos.write(line.getBytes());
                    System.out.print(line);
                    line = fileName + " (" + Math.round(size) + "KB)," + "HEliOS-D," + decompressionTime + "," + new File(source).length() + ",HEliOS decompression," + fileName + "\n";
                    bos.write(line.getBytes());
                    System.out.print(line);
                    line = fileName + " (" + Math.round(size) + "KB)," + "HEliOS-CS," + new File(compressedFile).length() + "," + new File(source).length() + ",HEliOS scale," + fileName + "\n";
                    bos.write(line.getBytes());
                    System.out.print(line);
                    line = fileName + " (" + Math.round(size) + "KB)," + "HEliOS-U," + uploadTime + "," + new File(source).length() + ",HEliOS upload," + fileName + "\n";
                    bos.write(line.getBytes());
                    System.out.print(line);
                    line = fileName + " (" + Math.round(size) + "KB)," + "HEliOS-D," + downloadTime + "," + new File(source).length() + ",HEliOS download," + fileName + "\n";
                    bos.write(line.getBytes());
                    System.out.print(line);

                     */
                }
                //break;
            }
            bos.close();
        } catch (Exception ex) {
                ex.printStackTrace();
        }
    }
}
