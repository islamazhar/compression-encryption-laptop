package utilities;



public class Element implements Comparable <Element>{
	public Integer curFrequency = 0;
	public  Character value = 0;
	public Integer level = 0;
	public Double weight = 0.0;
	/*
	public Element(Integer _count, Character _value, Integer _level){
		curFrequency = _count;
		value = _value;
		level = _level;
		weight = 0.00;
	}
	 */
	public Element(Integer _count, Character _value, Integer _level, Double _weight){
		curFrequency = _count;
		value = _value;
		level = _level;
		weight = _weight;

	}

	@Override
	public String toString() {
		return "{curFrequency = " + curFrequency +" weight = " +weight + " char =" + value + "}";
		
	}

	@Override
	public int compareTo(Element arg0) {
		if(!arg0.curFrequency.equals(curFrequency)) {
			return arg0.curFrequency-curFrequency;
		}

		if( Double.compare(arg0.weight, weight) !=0 ) {
			return Double.compare(arg0.weight, weight);
		}

		if(!arg0.level.equals(level)){
			return -arg0.level+level;
		}
		return -arg0.value+value;
	}
	public static void main(String[] args){
	    		RedBlackBST<Element> ts = new RedBlackBST<Element>();
	    		ts.put(new Element(0,'a',1, 0.0005));
	    		ts.put(new Element(0,'b',1, 0.0004));
	    		ts.put(new Element(0,'z',0,0.0003));
	    		Element e = new Element(0,'z',0,0.000110);
				ts.put(e);
	   			//System.out.println(ts.toString());
	   			System.out.println(ts.rank(e));
				Element e1 = new Element(0,'z',0,0.000110);
				System.out.println(ts.rank(e1));

	    }
}
