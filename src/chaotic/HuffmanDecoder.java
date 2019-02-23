/*************************************
 * @author mazharul.islam 
 * 
 **************************************/

package chaotic;
import utilities.*;

import java.io.BufferedOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;


public class HuffmanDecoder{
	PriorityQueue<HuffmanNode> q = null;
	Map<Integer,String> symbol  = null;
	public HuffmanDecoder(PriorityQueue<HuffmanNode> _q){
		q = _q;
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
			while(true) {
				//	System.out.println(cur.c);
				bit = bis.read();
				if(bit == -1) break;
				if(bit==0) {
					cur = cur.left;
				}
				else {
					cur = cur.right;
				}
				if(cur.isLeaf()) {
					bos.write(cur.c);
					cur = q.peek();
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
