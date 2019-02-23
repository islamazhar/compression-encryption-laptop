/**
 * @author mazharul.islam
 *
 */




import utilities.*;
import compression.*;
import chaotic.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;


public class Main {

	public static void main(String[] args) {
		///*
		chaoticHuffmanCompression huffman = new chaoticHuffmanCompression();
	//	Huffman huffman = new Huffman();
		String inputFileNamme = "test-data/2.txt";
		String compressedFileName = "test-data/2.zip";
		String decompressedFileName = "test-data/2.again.txt";
		huffman.compress(inputFileNamme, compressedFileName);
		huffman.deCompress(compressedFileName,decompressedFileName);
		
		/*
		double len1 = new File(inputFileNamme).length();
		double len2 = new File(compressedFileName).length();
		double len3 = new File(decompressedFileName).length();
		System.out.println(len1/len2);
		System.out.println(len1/len3);
		*/
	}
}



