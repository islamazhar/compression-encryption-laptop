package compression;

import encryption.ECC;
import file.transfer.DropboxClient;
import utilities.Files;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CHT {

    // CHT params
    private static final int R = 256;
    private static double x0 = 0.5;
    private static double p = 0.4;
    private static double alpha = 33.00;
    private static double beta = 33.00;
    private static int vm = 33;
    //private  static List<Node> huffmanNodes = null;
    // CHT params
    private static ECC ecc = null;


    public CHT() {
        ecc = new ECC();
    }



    private static void buildCode(String[] st, Node x, String s) {
        if (!x.isLeaf()) {
            buildCode(st, x.left, s + '0');
            buildCode(st, x.right, s + '1');
        } else {
            st[x.ch] = s;
        }
    }

    private static Node buildTrie(int[] freq) {

        // initialze priority queue with singleton trees
        MinPQ<Node> pq = new MinPQ<Node>();
        for (char i = 0; i < R; i++)
            if (freq[i] > 0)
                pq.insert(new Node(i, freq[i], null, null));

        // special case in case there is only one character with a nonzero frequency
        if (pq.size() == 1) {
            if (freq['\0'] == 0) pq.insert(new Node('\0', 0, null, null));
            else pq.insert(new Node('\1', 0, null, null));
        }

        // merge two smallest trees
        while (pq.size() > 1) {
            Node left = pq.delMin();
            Node right = pq.delMin();
            Node parent = new Node('\0', left.freq + right.freq, left, right);
            pq.insert(parent);
        }
        return pq.delMin();
    }

    private static void listNodes(Node x, List<Node> huffmanNodes) {
        if (!x.isLeaf()) {
            listNodes(x.left, huffmanNodes);
            listNodes(x.right, huffmanNodes);
        } else {
            huffmanNodes.add(x);
        }
    }

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

    public static long compress(String compressedFileName) {
        long t1 = System.currentTimeMillis();
        String s = BinaryStdIn.readString();
        char[] input = s.toCharArray();

        int[] freq = new int[R];
        for (int i = 0; i < input.length; i++) {
            freq[input[i]]++;
        }

        Node root = buildTrie(freq);
        String[] st = new String[R];
        buildCode(st, root, "");
        writeTrie(root);

        BinaryStdOut.close();
        BinaryStdOut.takeInputFile(compressedFileName);

        BinaryStdOut.write(input.length);

        List<Node> huffmanNodes = new ArrayList<>();
        listNodes(root, huffmanNodes);

        for (int i = 0; i < input.length; i++) {
            char ch = input[i];
            int n = 1 + vm % 10;
            double xn = chaoticMap(x0, n);
            Double r = Math.floor(xn * Math.pow(2.00, alpha) + vm * Math.pow(2.00, beta)) % huffmanNodes.size();
            Integer ri = r.intValue();


            x0 = xn;
            vm = ch;


            Node h = huffmanNodes.get(ri);
            h.swap();
            //System.out.println(ri);
            buildCode(st, root, "");
           // buildCode(st, h, st[h.ch]);
            String code = st[ch];

            for (int j = 0; j < code.length(); j++) {

                if (code.charAt(j) == '1') {
                    BinaryStdOut.write(true);
                } else if (code.charAt(j) == '0') {
                    BinaryStdOut.write(false);
                } else {
                    throw new IllegalStateException("Illegal state");
                }
            }
        }
        BinaryStdOut.close();
        long t2 = System.currentTimeMillis();
        return t2 - t1;
    }

    private static double chaoticMap(double x0, int n) {
        double x = x0;
        for (int i = 0; i < n; i++) {
            if (x <= p) {
                x = x / p;
            } else {
                x = (1 - x) / (1 - p);
            }
        }
        return x;
    }

    private static Node readTrie() {
        boolean isLeaf = BinaryStdIn.readBoolean();
        if (isLeaf) {
            return new Node(BinaryStdIn.readChar(), -1, null, null);
        } else {
            return new Node('\0', -1, readTrie(), readTrie());
        }
    }

    public static long expand( String compressedFile) {

        // read in Huffman trie from input stream
        long t1 = System.currentTimeMillis();
        Node root = readTrie();

        BinaryStdIn.close();
        BinaryStdIn.takeInputFile(compressedFile);

        // number of bytes to write
        int length = BinaryStdIn.readInt();
        // decode using the Huffman trie
        double xn = 0;
        String[] st = new String[R];
        List<Node> huffmanNodes = new ArrayList<>();
        listNodes(root, huffmanNodes);
        //for(Node x : huffmanNodes) {
          //  System.out.println(x);
        //}

        x0 = 0.5;
        vm = 33;

        for (int i = 0; i < length; i++) {

            int n = 1 + vm % 10;
            xn = chaoticMap(x0, n);
            Double r = Math.floor(xn * Math.pow(2.00, alpha) + vm * Math.pow(2.00, beta)) % huffmanNodes.size();
            Integer ri = r.intValue();


            Node h = huffmanNodes.get(ri);
            h.swap();
            buildCode(st, root, "");
            //buildCode(st, h, st[h.ch]);

            Node x = root;
            while (!x.isLeaf()) {
                boolean bit = BinaryStdIn.readBoolean();
                if (bit) x = x.right;
                else x = x.left;
            }

            BinaryStdOut.write(x.ch, 8);
            x0 = xn;
            vm = x.ch;
        }
        BinaryStdOut.close();
        long t2 = System.currentTimeMillis();

        return t2 - t1;
    }
    public double compress(String inputFileNamme, String compressed) {
        String tree = compressed+".tree";
        BinaryStdIn.takeInputFile(inputFileNamme);
        BinaryStdOut.takeInputFile(tree);
        double t = 0;
        t += compress(compressed);
        // System.err.println("Compression = "+t);
        double encryptionTime = ecc.encryption(tree,tree+".encrypted");
        // System.err.println("Encryption = "+tt);
        t += encryptionTime;
        return t;
    }

    public double deCompress (String compressed, String outputFile) {
        String tree = compressed+".tree";
        double decryptionTime = ecc.decryption(compressed+".tree.encrypted",tree);
        BinaryStdIn.takeInputFile(compressed);
        BinaryStdOut.takeInputFile(outputFile);
        double t = expand(compressed);
        t += decryptionTime;
        return t;
    }


    public static void main(String[] args) throws IOException {
       // String filetype = "songs";
        String filetype = "office";
      //  String filetype = "video";
       // String filetype = "ebooks";
        DropboxClient dropboxClient = new DropboxClient();
        String dropboxDirectory = "/upload/";
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File("results-laptop-dropbox.csv"), true));
        String directoryAddress = "/Users/mazharul.islam/Documents/format-corpus/"+filetype+"/";
        String outputDirectoryAddress = "/Users/mazharul.islam/Documents/format-corpus/"+filetype+"/CHT/";
        Files files = new Files(directoryAddress);
        List<String> filesName = files.getFiles();
        try {
            for(String fileName: filesName) {
                String source = directoryAddress + fileName;
                System.out.println(source);
                String compressedFile = outputDirectoryAddress + fileName +  ".cht";
                //String encryptedCompressed = outputDirectoryAddress + fileName + ".cht.en";
                String outFile = outputDirectoryAddress + fileName + ".cht.again.txt";
                for (int i = 0; i < 1; i++) {
                    CHT chaoticHuffmanTree = new CHT();
                    double tim1 = chaoticHuffmanTree.compress(source, compressedFile);
                    double uploadTime = dropboxClient.HTTPUP(compressedFile,dropboxDirectory+fileName);
                    double tim2 = chaoticHuffmanTree.deCompress(compressedFile, outFile);
                    double downloadTime = dropboxClient.HTTPUP(compressedFile,dropboxDirectory+fileName);
                    dropboxClient.delete(dropboxDirectory+fileName);
                    double size = new File(source).length() / 1000.00;
                    String line = fileName + " (" + Math.round(size) + "KB)," + "cht-U," + tim1 + "," + new File(source).length() + ",Chaotic Huffman Tree (CHT) compression," + filetype+"\n";
                    bos.write(line.getBytes());
                    System.out.print(line);
                    line = fileName + " (" + Math.round(size) + "KB)," + "cht-D," + tim2 + "," + new File(source).length() + ",Chaotic Huffman Tree (CHT) decompression," + filetype+"\n";
                    bos.write(line.getBytes());
                    System.out.print(line);
                    line = fileName + " (" + Math.round(size) + "KB)," + "cht-CS," + new File(compressedFile).length() + "," + new File(source).length() + ",Chaotic Huffman Tree (CHT) scale," + filetype+"\n";
                    bos.write(line.getBytes());
                    System.out.print(line);
                    line = fileName + " (" + Math.round(size) + "KB)," + "cht-U," + uploadTime + "," + new File(source).length() + ",Chaotic Huffman Tree (CHT) upload," + filetype+"\n";
                    bos.write(line.getBytes());
                    System.out.print(line);
                    line = fileName + " (" + Math.round(size) + "KB)," + "cht-D," + downloadTime + "," + new File(source).length() + ",Chaotic Huffman Tree (CHT) download," + filetype+"\n";
                    bos.write(line.getBytes());
                    System.out.print(line);
                }
               // break;
            }
            bos.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
