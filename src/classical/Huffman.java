package classical;
/*************************************
 * @author mazharul.islam 
 * 
 **************************************/

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import encryption.ECC;

import java.util.List;


import utilities.BitInputStream;
import utilities.BitOutputStream;
import utilities.HuffmanNode;


public class Huffman {

	//	int [] freq = new int [256];
	//	char [] chars = new char [256];
	PriorityQueue<HuffmanNode>pq = null;
	Map<Integer,String> symbol = null;
	Map<Integer, Integer> mapCopy = null;
	long total = 0;
	public Huffman() {
	}

	public long compress(String inputFileNamme, String compressedFileName) {

		File inputFile = new File(inputFileNamme);
		File outputFile = new File(compressedFileName);


		Map<Integer,Integer> map=new HashMap<Integer,Integer>();
		symbol=new HashMap<Integer,String>();

		try {
			BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile)));
			int ch = 0;
			while((ch = bf.read())!=-1) {
				//System.out.println(ch);
				if(map.containsKey(ch)) {
					map.put(ch,map.get(ch)+1);
				}
				else {
					map.put(ch, 1);
				}
			}

			HuffmanEncoder huffmanEncoder = new HuffmanEncoder(map);
			mapCopy = new HashMap<Integer, Integer>(map);
			pq = huffmanEncoder.buildTree(); 
			// pq is the original Huffman tree which is a priority queue
			//pq.peek().encodedString = "";
			huffmanEncoder.buildHuffmanTreeTable(pq.peek(), "");

			//System.out.println("size = " + pq.size());
			//huffmanEncoder.print(pq.peek());

			// building the Huffman tree table from Huffman tree pq
			//double len2 = huffmanEncoder.len/8.00;
			//System.out.println("Compression ratio : "+len1/len2);
			bf.close();
			// second pass on the input file

			bf = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile)));
			BitOutputStream bos = new BitOutputStream( new FileOutputStream(outputFile));
			symbol = huffmanEncoder.symbol;


			map.clear();
			map = new HashMap<Integer,Integer>();

			long s = System.currentTimeMillis();

			while((ch = bf.read())!=-1) {
				String encodedString = symbol.get(ch);

				for(int  i=0;i<encodedString.length();i++) {
					if(encodedString.charAt(i)=='1') {
						bos.write(1);
					}
					else {
						bos.write(0);
					}
				}
			}
			long e = System.currentTimeMillis();
			total = e-s;
			//System.out.println("chaotic Huffman tree Encoding Time = "+ (e-s));
			bos.close();
			bf.close();
			return e-s;
			//	*/
		}catch(Exception ex) {
			System.out.println("while  compressing " + ex.toString());
			ex.printStackTrace();
		}
		return 0;
	}



	public long deCompress(String compressedFileName, String outputFileName) {
		File inputFile = new File(compressedFileName);
		File outputFile = new File(outputFileName);
		try {
			InputStream is = new BufferedInputStream(new FileInputStream(inputFile));
			BitInputStream bis = new BitInputStream(is);
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outputFile));
			
			
			
			HuffmanDecoder huffmanDecoder = new HuffmanDecoder(mapCopy);
			long s = System.currentTimeMillis();
			huffmanDecoder.decoder(bis, bos);
			long e = System.currentTimeMillis();
			//System.out.println("chaotic Huffman tree decoding Time = "+ (e-s));
			bis.close();
			bos.close();
			return e-s;
		}catch(Exception ex) {
			System.out.println("while decompressing " + ex.toString());
			ex.printStackTrace();
		}
		return 0;
	}


	public static void main(String[] args) {
		
		Huffman huffman=new Huffman();
		String inputFileNamme = "test-data/sizes/"+String.valueOf(1024)+".txt";
		String compressedFileName = "test-data/sizes/"+String.valueOf(1024)+".zip";
		
		huffman.compress(inputFileNamme, compressedFileName);
		ECC ecc = new ECC();
		byte[] encryptedBytes = ecc.encryption(huffman.mapCopy.toString().getBytes());
		byte[] decryptedBytes = ecc.decryption(encryptedBytes);
		
		
		
		
		///*
		//	chaoticHuffmanCompression huffman = new chaoticHuffmanCompression();
		/*
		for(Integer siz = 1024;siz<=2097152;siz*=2) {
			long mean1 = 0;
			long mean2 = 0;
			String inputFileNamme = "test-data/sizes/"+String.valueOf(siz)+".txt";
			String compressedFileName = "test-data/sizes/"+String.valueOf(siz)+".zip";
			String decompressedFileName = "test-data/sizes/"+String.valueOf(siz)+".again.txt";
			Huffman huffman = new Huffman();
			*/
		//	double t1 = 0.0,t2=0.0;
		//	for(int i=0;i<20;i++) {

		//		mean1 += huffman.compress(inputFileNamme, compressedFileName);
		//		mean2 += huffman.deCompress(compressedFileName,decompressedFileName);
		//		t1 +=mean1;
		//		t1 +=mean2;
		//	}
	//	}

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


	}

}

