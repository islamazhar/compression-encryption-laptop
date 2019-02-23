
package utilities;
import java.util.Comparator;

// comparator class helps to compare the node 
// on the basis of one of its attribute. 
// Here we will be compared 
// on the basis of data values of the nodes. 

public class MyComparator implements Comparator<HuffmanNode> { 
	public int compare(HuffmanNode x, HuffmanNode y) 
	{
		return x.data - y.data; 
	} 
}