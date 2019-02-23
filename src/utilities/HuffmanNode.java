
package utilities;



// node class is the basic structure 
// of each node present in the huffman - tree. 

public class HuffmanNode { 
	public int data; 
	public Integer c; 
	public HuffmanNode left; 
	public HuffmanNode right;
	public boolean isLeaf() {
		if(left  == null && right  == null) {
				return true;
		}
		else {
			return false;
		}
	}
	public void  swap() {
		HuffmanNode t = left;
		left = right;
		right = t;
	}
} 