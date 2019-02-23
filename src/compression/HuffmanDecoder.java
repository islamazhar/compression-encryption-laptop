/*************************************
 * @author mazharul.islam 
 * 
 **************************************/

package compression;
import utilities.*;

import java.io.BufferedOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import javax.swing.text.AbstractDocument.LeafElement;

public class HuffmanDecoder{
	PriorityQueue<HuffmanNode> q = null;
	//List<RedBlackBST<Element, Integer>> HT = null;
	//List<AVLTreeST<Element, Integer>>HT = null;
	List<RedBlackBST<Element, Integer>>HT = null;
	ArrayList<Map<String, Integer>> list = null;
	Map<Integer,String> symbol  = null;
	Map<Integer,Integer> cnt  = null;
	public HuffmanDecoder(PriorityQueue<HuffmanNode> _q, ArrayList<Map<String,Integer>> _list, 
			Integer maxLevel, Map<Integer,String> _symbol){
		q = _q;
		list = _list;
		HT = new ArrayList<>();
		//list = new ArrayList< Map<String, Integer>>();
		// for each level we will create a  AVL tree and a list of encoded strings
		for(int i=0;i<maxLevel;i++) {
			HT.add(new RedBlackBST<Element, Integer>()) ;
		}
		
		for(Integer num: _symbol.keySet()) {
			int len = _symbol.get(num).length();
			RedBlackBST<Element,Integer> h = HT.get(len);
			h.put(new Element(0, num), 0);
		}

		cnt = new HashMap<Integer,Integer>();
	}

	/* for unit  testing purposes */
	public void decoder(String str) {
		HuffmanNode cur = q.peek();

		for(int i=0;i<str.length();i++) {
			//	System.out.println(cur.c);

			if(str.charAt(i)=='0') {
				cur = cur.left;
			}
			else {
				cur = cur.right;
			}
			if(cur.isLeaf()) {
				System.out.print(cur.c);
				cur = q.peek();
			}
		}
	}


	public void decoder(BitInputStream bis, BufferedOutputStream bos) {
		try {
			HuffmanNode cur = q.peek();
			int bit;
			String encodedString = "";
			while(true) {
				//	System.out.println(cur.c);
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
					
					RedBlackBST<Element,Integer> avl = HT.get(l);
					int pos = list.get(l).get(encodedString);
					
					int decodedValue = avl.select(pos).value;
					
					if(!cnt.containsKey(decodedValue)) {
	    	 				cnt.put(decodedValue,0);
	    	 			}
					bos.write(decodedValue);
					avl.delete(avl.select(pos));
					cnt.put(decodedValue, cnt.get(decodedValue)+1);
					avl.put(new Element(cnt.get(decodedValue), decodedValue),0);
					cur = q.peek();
					encodedString = "";
				}
			}
		}catch(Exception ex) {
			System.out.println("while decoding " + ex.toString());
			ex.printStackTrace();
		}
	}


	public static void main(String[] args) {

	}

}
