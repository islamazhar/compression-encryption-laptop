
package utilities;



// node class is the basic structure 
// of each node present in the huffman - tree. 

public class HuffmanNode { 
	public int data; 
	public Integer c; 
	public HuffmanNode left;
	public String leftSymbol;
	public HuffmanNode right;
	public String encodedString = null;
	public HuffmanNode() {
		encodedString = "";
	}
	public boolean isLeaf() {
		if(left == null && right == null) { // -1 means not  a tree
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