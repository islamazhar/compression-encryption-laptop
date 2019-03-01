/*************************************
 * @author mazharul.islam 
 * 
 **************************************/

package ourmethod;



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
import java.util.List;


import utilities.BitInputStream;
import utilities.BitOutputStream;
import utilities.HuffmanNode;
import utilities.RedBlackBST;
import utilities.Element;

public class Huffman {
	
	List<RedBlackBST<Element>> HT = null;

	List<Map<Integer, String>> list = null; // extra integer and encodedString  for each level hence this is a list
	Map<String, Integer> list2 = null; // extra 
	Map<Integer,String> symbol = null;
	Map<Integer,Integer> len = null; // extra
	Integer maxLevel = 0; // extra
	HuffmanNode root = null;
	public Huffman() {
	}
	
	public long compress(String inputFileNamme, String compressedFileName) {
		
		File inputFile = new File(inputFileNamme);
		File outputFile = new File(compressedFileName);
		
		
		Map<Integer,Integer> map=new HashMap<Integer,Integer>();
		symbol=new HashMap<Integer,String>();
		len = new HashMap <Integer,Integer>();

		try {
			BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile)));
			int ch = 0;
	
			while((ch = bf.read())!=-1) {
				if(map.containsKey(ch)) {
					map.put(ch,map.get(ch)+1);
				}
				else {
					map.put(ch, 1);
				}
			
			}
			HuffmanEncoder huffmanEncoder = new HuffmanEncoder(map);
			root = huffmanEncoder.buildTree(); 
			huffmanEncoder.buildHuffmanTreeTable(root, ""); 
			bf.close();
			bf = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile)));
			BitOutputStream bos = new BitOutputStream( new FileOutputStream(outputFile));
			symbol = huffmanEncoder.symbol;
			
			maxLevel = 0;
			
			for(Integer num: symbol.keySet()) {
				int level = symbol.get(num).length();
				len.put(num, level);
				maxLevel = Math.max(level+1, maxLevel);
			}
			
			HT = new ArrayList<>();
			
			
			list = new ArrayList< Map<Integer, String>>();
			list2 = new HashMap<String,Integer>();
			
			
			for(int i=0;i<maxLevel;i++) {
				HT.add(new RedBlackBST<Element>());
				list.add( new HashMap());
			}
			
			for(Integer num: symbol.keySet()) {
				int l = len.get(num);
				HT.get(l).put(new Element(0,num));
				int  siz = HT.get(l).size()-1;
				list.get(l).put(siz, symbol.get(num));
				list2.put(symbol.get(num), siz);
			}
			
			
			map.clear();

			
			long s = System.currentTimeMillis();
	
			while((ch = bf.read())!=-1) {
	
				int l = len.get(ch);
				RedBlackBST<Element> h = HT.get(l); 
				Map<Integer, String> listt =  list.get(l);
				
				if(!map.containsKey(ch)) {
					map.put(ch,0);
				}
				int siz = map.get(ch);
				
				Element e = new Element(siz,ch);
				int pos = h.rank(e); // log n

			
				String encodedString = listt.get(pos);
			
				for(int  i=0;i<encodedString.length();i++) {
					if(encodedString.charAt(i)=='1') {
						bos.write(1);
					}
					else {
						bos.write(0);
					}
				}
				h.delete(e); 
				map.put(ch, siz+1);  // increase the weigth by 1

				
				h.put(new Element(e.count+1,e.value));
			}
			long e = System.currentTimeMillis();
			
			bos.close();
			bf.close();
			return e-s;
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
			HuffmanDecoder huffmanDecoder = new HuffmanDecoder(root,list2, maxLevel, symbol);
			long s = System.currentTimeMillis();
			huffmanDecoder.decoder(bis, bos);
			long e = System.currentTimeMillis();
			bis.close();
			bos.close();
			return e-s;
		}catch(Exception ex) {
			System.out.println("while decompressing " + ex.toString());
			ex.printStackTrace();
		}
		return 0;
	}

	
	//public static void main(String[] args) {
    		
    //}

}
