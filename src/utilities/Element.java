package utilities;



public class Element implements Comparable <Element>{
	public Integer curFrequency = 0;
	public  Character value = 0;
	public Integer level = 0;
	
	public Element(Integer _count, Character _value, Integer _level){
		curFrequency = _count;
		value = _value;
		level = _level;
	}


	@Override
	public String toString() {
		return "{level = "+level+" curFrequency = " + curFrequency +" char =" + value+"}";
		
	}

	@Override
	public int compareTo(Element arg0) {
		if(!arg0.level.equals(level)){
			return -arg0.level+level;
		}

		if(!arg0.curFrequency.equals(curFrequency)) {
			return -arg0.curFrequency+curFrequency;
		}
		return -arg0.value+value;
	}
	public static void main(String[] args){
	    		RedBlackBST<Element> ts = new RedBlackBST<Element>();
	    		ts.put(new Element(138,'a',1));
	    		ts.put(new Element(138,'b',1));
	    		ts.put(new Element(140,'z',0));
		ts.put(new Element(138,'z',0));
	    		System.out.println(ts.toString());
	    }
}
