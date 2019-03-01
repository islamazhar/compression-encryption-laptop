/*************************************
 * @author mazharul.islam 
 * 
 **************************************/

package ourmethod;
import utilities.*;

import java.io.BufferedOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;


public class HuffmanDecoder{
	HuffmanNode q = null;
	List<RedBlackBST<Element>>HT = null;
	Map<String, Integer> list = null; // the permutation
	Map<Integer,String> symbol  = null;
	
	public HuffmanDecoder(HuffmanNode _q, Map<String,Integer> _list, 
			Integer maxLevel, Map<Integer,String> _symbol){
		q = _q;
		list = _list;
		HT = new ArrayList<>();
		for(int i=0;i<maxLevel;i++) {
			HT.add(new RedBlackBST<Element>()) ;
		}
		
		for(Integer num: _symbol.keySet()) {
			int len = _symbol.get(num).length();
			RedBlackBST<Element> h = HT.get(len);
			h.put(new Element(0, num));
		}

	}

	/* for unit  testing purposes */
	public void decoder(String str) {
		HuffmanNode cur = q;

		for(int i=0;i<str.length();i++) {

			if(str.charAt(i)=='0') {
				cur = cur.left;
			}
			else {
				cur = cur.right;
			}
			if(cur.isLeaf()) {
				cur = q;
			}
		}
	}


	public void decoder(BitInputStream bis, BufferedOutputStream bos) {
		try {
			HuffmanNode cur = q;
			int bit;
			String encodedString = "";
			while(true) {
				bit = bis.read();
				if(bit == -1) break;
				if(bit==0) {
					encodedString = encodedString + "0";
					cur = cur.left;
				}
				else {
					encodedString = encodedString + "1";
					cur = cur.right;
				}
				if(cur.isLeaf()) {
					int l = encodedString.length();
					
					RedBlackBST<Element> avl = HT.get(l);

					int pos = list.get(encodedString);
					
					Element e = avl.select(pos);
					int decodedValue = e.value;
					bos.write(decodedValue);
					avl.delete(e);
					avl.put(new Element(e.count+1,e.value));
					cur = q;
					encodedString = "";
				}
			}
		}catch(Exception ex) {
			System.out.println("while decoding " + ex.toString());
			ex.printStackTrace();
		}
	}


//	public static void main(String[] args) {

//	}

}
