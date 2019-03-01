package utilities;

import java.util.TreeSet;

public class Element implements Comparable <Element>{
	public String encodedString = null;
	public Integer count = 0;
	public  Integer value = 0;
	
	public Element(Integer _count, Integer _value){
		encodedString = "";
		count = _count;
		value = _value;
	}
	@Override
	public boolean equals(Object arg0) {
		Element t = (Element) arg0;
		return t.count == count && 
			//	t.encodedString.equals(encodedString) && 
				t.value == value;
	}
	@Override
	public int hashCode() {
		return value.hashCode() ;
	}
	@Override
	public String toString() {
		return "{count = " + count + ",encodedString = " + 
				encodedString + ",value =" + value+"}"; 
		
	}
	@Override
	public int compareTo(Element arg0) {
		
	//	/*
		
		if(!arg0.count.equals(count)) {
			//System.out.println(arg0.count + " "+count);
			return -arg0.count+count;
		}
	//	else if(!arg0.encodedString.equals(encodedString)) {
	//		return arg0.encodedString.compareTo(encodedString);
	//	}
	//	System.out.println(this.toString());
	//	System.out.println(arg0.toString());
	//	System.out.println(-arg0.value+value);
		return -arg0.value+value;
	//	*/
		/* 
		if(arg0.count != count) { 
			if(arg0.count<count) return 1;
			return -1;
		}
		else if( arg0.value != value) {
			if(arg0.value < value) return 1;
			return -1;
		}
		else {
			return arg0.encodedString.compareTo(encodedString);
		}
	//	*/
		
		//return encodedString.hashCode() - arg0.encodedString.hashCode();
	}
	public static void main(String[] args) { 
	     //   TreeSet<Element> ts = new TreeSet<>();
	       // ts.
	    		RedBlackBST<Element> ts = new RedBlackBST<Element>();
	    		ts.put(new Element(138,111));
	    		ts.put(new Element(138,115));
	    		System.out.println(ts.toString());
	    }
}
