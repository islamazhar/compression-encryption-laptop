/*************************************
 * @author mazharul.islam 
 * 
 **************************************/

package compression;



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

public class Huffman {
	int [] freq = new int [256];
	char [] chars = new char [256];
	PriorityQueue<HuffmanNode>pq = null;
	//List<RedBlackBST<Element, Integer>> HT = null;
	//List<AVLTreeST<Element, Integer>> HT = null;
	List<RedBlackBST<Element, Integer>> HT = null;
	//<TreeSet<Element>> HT = null;
	List<Map<Integer, String>> list = null; // integer and encodedString 
	ArrayList<Map<String, Integer>> list2 = null;
	Map<Integer,String> symbol = null;
	Integer maxLevel = 0;
	public Huffman() {
	}
	
	public void compress(String inputFileNamme, String compressedFileName) {
		
		File inputFile = new File(inputFileNamme);
		File outputFile = new File(compressedFileName);
		
		
		Map<Integer,Integer> map=new HashMap<Integer,Integer>();
		symbol=new HashMap<Integer,String>();

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
			pq = huffmanEncoder.buildTree(); 
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
			
			maxLevel = 0;
			
			for(Integer num: symbol.keySet()) {
				int level = symbol.get(num).length();
				maxLevel = Math.max(level+1, maxLevel);
			}
			
			//System.out.println("Max value is = " + maxLevel);
			HT = new ArrayList<>();
			
			
			list = new ArrayList< Map<Integer, String>>();
			list2 = new ArrayList< Map<String, Integer>>();
			
			// for each level we will create a  AVL tree and a list of encoded strings
			for(int i=0;i<maxLevel;i++) {
				HT.add(new RedBlackBST<Element, Integer>());
				list.add( new HashMap());
				list2.add( new HashMap());
			}
			
			for(Integer num: symbol.keySet()) {
				//System.out.println(num);
				int len = symbol.get(num).length();
				//System.out.println(idx);
				HT.get(len).put(new Element(0,num),0);
				int  siz = HT.get(len).size()-1;
			//	System.out.println(" size "+ siz + " symbol "+symbol.get(num));
				list.get(len).put(siz, symbol.get(num));
				list2.get(len).put(symbol.get(num), siz);
				//System.out.println(num + " "+levelValue[num]);
			}
	//		/*
			for(int i=0;i<maxLevel;i++) {
				System.out.println(HT.get(i).size());
				//System.out.println(list.get(i));
			}
	//		*/
			
		
			map.clear();
			map = new HashMap<Integer,Integer>();
			
			while((ch = bf.read())!=-1) {
				String sym = symbol.get(ch);
				int l = sym.length();
				RedBlackBST<Element, Integer> h = HT.get(l);
				
				if(!map.containsKey(ch)) {
					map.put(ch,0);
				}
				//System.out.println(h.toString());
				int pos = h.rank(new Element(map.get(ch),ch));
				//pos = h.get(h.select(pos));
				//System.out.println(ch + " "+map.get(ch) + " "+sym+" pos " + pos);
				// find the index of a (a b c) then h.get(a) = 0 (index not the frequency)
				
				//System.out.println(list.get(l).toString());
				String encodedString = list.get(l).get(pos);
			
				//System.out.println(encodedString);
				for(int  i=0;i<encodedString.length();i++) {
					if(encodedString.charAt(i)=='1') {
						bos.write(1);
					}
					else {
						bos.write(0);
					}
				}
				h.delete(new Element(map.get(ch),ch));
				map.put(ch, map.get(ch)+1);  // increase the weigth by 1
				h.put(new Element(map.get(ch),ch),0);
			}
			
			bos.close();
			bf.close();
		//	*/
		}catch(Exception ex) {
			System.out.println("while  compressing " + ex.toString());
			ex.printStackTrace();
		}
	}
	
	public void deCompress(String compressedFileName, String outputFileName) {
		File inputFile = new File(compressedFileName);
		File outputFile = new File(outputFileName);
		try {
			InputStream is = new BufferedInputStream(new FileInputStream(inputFile));
			BitInputStream bis = new BitInputStream(is);
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outputFile));
			HuffmanDecoder huffmanDecoder = new HuffmanDecoder(pq, list2, maxLevel, symbol);
			huffmanDecoder.decoder(bis, bos);
			bis.close();
			bos.close();
		}catch(Exception ex) {
			System.out.println("while decompressing " + ex.toString());
			ex.printStackTrace();
		}
	}
	public static double[] xcorr(double[] a, double[] b)
    {
        int len = a.length;
        if(b.length > a.length)
            len = b.length;

        return xcorr(a, b, len-1);

        // // reverse b in time
        // double[] brev = new double[b.length];
        // for(int x = 0; x < b.length; x++)
        //     brev[x] = b[b.length-x-1];
        // 
        // return conv(a, brev);
    }
	public static double[] xcorr(double[] a, double[] b, int maxlag)
    {
        double[] y = new double[2*maxlag+1];
        Arrays.fill(y, 0);
        
        for(int lag = b.length-1, idx = maxlag-b.length+1; 
            lag > -a.length; lag--, idx++)
        {
            if(idx < 0)
                continue;
            
            if(idx >= y.length)
                break;

            // where do the two signals overlap?
            int start = 0;
            // we can't start past the left end of b
            if(lag < 0) 
            {
                //System.out.println("b");
                start = -lag;
            }

            int end = a.length-1;
            // we can't go past the right end of b
            if(end > b.length-lag-1)
            {
                end = b.length-lag-1;
                //System.out.println("a "+end);
            }

            //System.out.println("lag = " + lag +": "+ start+" to " + end+"   idx = "+idx);
            for(int n = start; n <= end; n++)
            {
                //System.out.println("  bi = " + (lag+n) + ", ai = " + n); 
                y[idx] += a[n]*b[lag+n];
            }
            //System.out.println(y[idx]);
        }
        return(y);
    }
	
	public static void main(String[] args) {
    		double[] a = {1,2,3,4,15};
    		double[] b = {1,2,3,4,15};
    		double[] y = xcorr(b, a,0);
        	//System.out.println(y.toString());
    		
        	for(int i=0;i<y.length;i++){
        		System.out.println(y[i]);
        		//System.out.println(y[i]);
        	}
    }

}
