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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.List;

import utilities.AVLTreeST;
import utilities.BitInputStream;
import utilities.BitOutputStream;
import utilities.HuffmanNode;
import utilities.RedBlackBST;
import utilities.Element;

public class chaoticHuffmanCompression {

		int [] freq = new int [256];
		char [] chars = new char [256];
		PriorityQueue<HuffmanNode>pq = null;
		Map<Integer,String> symbol = null;
		List<HuffmanNode> huffmannodes = null;
		Integer maxLevel = 0;
		double x0 = 0.5;
		double p = 0.4;
		 double alpha = 33.00;
		 double beta = 33.00;
		public chaoticHuffmanCompression() {
		}
		
		public void compress(String inputFileNamme, String compressedFileName) {
			
			File inputFile = new File(inputFileNamme);
			File outputFile = new File(compressedFileName);
			
			
			Map<Integer,Integer> map=new HashMap<Integer,Integer>();
			symbol=new HashMap<Integer,String>();
			huffmannodes = new ArrayList();
			try {
				BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile)));
				int ch = 0;
				int len1 = 0;
				while((ch = bf.read())!=-1) {
					//System.out.println(ch);
					if(map.containsKey(ch)) {
						map.put(ch,map.get(ch)+1);
					}
					else {
						map.put(ch, 1);
					}
					len1++;
				}
				HuffmanEncoder huffmanEncoder = new HuffmanEncoder(map);
				pq = huffmanEncoder.buildTree(huffmannodes); 
				// pq is the original Huffman tree which is a priority queue
				huffmanEncoder.buildHuffmanTreeTable(pq.peek(), ""); 
				// building the Huffman tree table from Huffman tree pq
				double len2 = huffmanEncoder.len/8.00;
				System.out.println("Compression ratio : "+len1/len2);
				bf.close();
				// second pass on the input file
				
				bf = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile)));
				BitOutputStream bos = new BitOutputStream( new FileOutputStream(outputFile));
				symbol = huffmanEncoder.symbol;
				
				
				map.clear();
				map = new HashMap<Integer,Integer>();
				
				long s = System.currentTimeMillis();
				
				while((ch = bf.read())!=-1) {
					
					int n = 1 + ch%10;
					double xn = chaoticMap(x0,n);
					Double r = Math.floor(xn *Math.pow(2.00, alpha) + 
							ch * Math.pow(2.00, beta)) % huffmannodes.size();
					Integer ri = r.intValue();
					
					//System.out.println(n + " " + ri);
					x0 = xn;

					
					HuffmanNode h = huffmannodes.get(ri);
					
					// update the tree
					h.swap();
					huffmanEncoder.buildHuffmanTreeTable(pq.peek(), ""); 
					
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
				System.out.println("chaotic Huffman tree Encoding Time = "+ (e-s));
				bos.close();
				bf.close();
			//	*/
			}catch(Exception ex) {
				System.out.println("while  compressing " + ex.toString());
				ex.printStackTrace();
			}
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

		public void deCompress(String compressedFileName, String outputFileName) {
			File inputFile = new File(compressedFileName);
			File outputFile = new File(outputFileName);
			try {
				InputStream is = new BufferedInputStream(new FileInputStream(inputFile));
				BitInputStream bis = new BitInputStream(is);
				BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outputFile));
				HuffmanDecoder huffmanDecoder = new HuffmanDecoder(pq);
				huffmanDecoder.decoder(bis, bos);
				bis.close();
				bos.close();
			}catch(Exception ex) {
				System.out.println("while decompressing " + ex.toString());
				ex.printStackTrace();
			}
		}

		
	//	public static void main(String[] args) {
	  //  		
	    //}

	}

