package utilities;

import java.util.TreeSet;

public class Element implements Comparable <Element>{
	public String encodedString = null;
	public Integer count = 0;
	public  Integer value = 0;
	public Element(String _encodedString, Integer _count){
		encodedString = _encodedString;
		count = _count;
		value = 0;
	}
	public Element(Integer _count, Integer _value){
		encodedString = "";
		count = _count;
		value = _value;
	}
	@Override
	public boolean equals(Object arg0) {
		Element t = (Element) arg0;
		return t.count == count && t.encodedString.equals(encodedString) && t.value == t.value;
	}
	@Override
	public int hashCode() {
		return encodedString.hashCode() + count.hashCode();
	}
	@Override
	public String toString() {
		return " couunt = " + count + " encodedString " + 
				encodedString + " value " + value; 
		
	}
	@Override
	public int compareTo(Element arg0) {
		
		
		if(arg0.count != count) 
			return -arg0.count + count;
		else if(!encodedString.equals(arg0.encodedString))
				return encodedString.compareTo(arg0.encodedString);
		else return -arg0.value+value;
		//return encodedString.hashCode() - arg0.encodedString.hashCode();
	}
	public static void main(String[] args) { 
	       // TreeSet<Integer> ts = new TreeSet<>();
	       // ts.
	    		TreeSet<Element> ts = new TreeSet<Element>();
	    		ts.add(new Element(12,65));
	    		ts.add(new Element(13,67));
	    		ts.add(new Element(13,68));
	    		ts.add(new Element(20,68));
	    		System.out.println(ts.lower(new Element(20,68)));
	    		ts.remove(new Element(13,68));
	    		
	    		System.out.println(ts.lower(new Element(20,68)));
	    }
}
