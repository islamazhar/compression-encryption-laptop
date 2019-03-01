package chaotic;
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


public class chaoticHuffmanCompression {

	//	int [] freq = new int [256];
	//	char [] chars = new char [256];
		PriorityQueue<HuffmanNode>pq = null;
		Map<Integer,String> symbol = null;
		List<HuffmanNode> huffmannodes = null;
		Integer maxLevel = 0;
		double x0 = 0.5;
		double p = 0.4;
		double alpha = 33.00;
		double beta = 33.00;
		int vm = 33;
		//Map<Integer, Integer> mapCopy = null;
		long total = 0;
		byte[] encrypted = null;
		ECC ecc = null;
		public chaoticHuffmanCompression() {
			ecc = new ECC();
		}
		
		public long compress(String inputFileNamme, String compressedFileName) {
			
			File inputFile = new File(inputFileNamme);
			File outputFile = new File(compressedFileName);
			
			
			Map<Integer,Integer> map=new HashMap<Integer,Integer>();
			symbol=new HashMap<Integer,String>();
			huffmannodes = new ArrayList<HuffmanNode>();
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

				encrypted = ecc.encryption(map.toString().getBytes());
				//mapCopy = new HashMap<Integer, Integer>(map);
				pq = huffmanEncoder.buildTree(); 
				// pq is the original Huffman tree which is a priority queue
				//pq.peek().encodedString = "";
				huffmanEncoder.buildHuffmanTreeTable(pq.peek(), "");
				huffmanEncoder.addNodes(pq.peek(), huffmannodes);
				
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
					
					int n = 1 + vm%10;
					double xn = chaoticMap(x0,n);
					//System.out.println(xn);
					Double r = Math.floor(xn *Math.pow(2.00, alpha) + 
							vm * Math.pow(2.00, beta)) % huffmannodes.size();
					Integer ri = r.intValue();
					
					//System.out.println(n + " " + ri);
					x0 = xn;
					vm = ch;

					
					HuffmanNode h = huffmannodes.get(ri);
					
					// update the tree
					h.swap();
					//h  = pq.peek();
				//	System.out.println("R "+ri);
				//	huffmanEncoder.print(pq.peek());
					huffmanEncoder.buildHuffmanTreeTable(h, h.encodedString);
				//	System.out.println("------------------------");
				//	huffmanEncoder.print(pq.peek());
				//	System.out.println("=======================");
					
					// number the trees keep them in a list.
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
		
		private double chaoticMap(double x0,int n) {
			double x = x0;
			for(int i=0;i<n;i++) {
				if(x<=p) {
					x = x/p;
				}
				else {
					x = (1-x)/(1-p);
				}
			}
			return x;
		}

		public long deCompress(String compressedFileName, String outputFileName) {
			File inputFile = new File(compressedFileName);
			File outputFile = new File(outputFileName);
			try {
				InputStream is = new BufferedInputStream(new FileInputStream(inputFile));
				BitInputStream bis = new BitInputStream(is);
				BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outputFile));
				Map<Integer,Integer> map1 = ecc.decryptionMap(encrypted);
				HuffmanDecoder huffmanDecoder = new HuffmanDecoder(map1);
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

		
	//	public static void main(String[] args) {
	  //  		
	    //}

	}

