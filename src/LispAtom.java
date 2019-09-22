import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;


public class LispAtom extends LispList  {
	private String atom;
	private BigDecimal atomAsNumber;
	// A hash map of all the defined lisp functions
	private static HashMap<LispAtom, LispFunction> functions;

	static final Logger logger = Logger.getLogger(LispAtom.class);

	static {
		logger.setLevel(Level.INFO); 
		initializeFunctions();
	}
	

	static void initializeFunctions() {

		// A hash map of all the defined lisp functions
		functions = new HashMap<LispAtom, LispFunction>();

		functions.put(new LispAtom("+"), LispList::sumFlatList);
		functions.put(new LispAtom("quote"), LispList::quote);
		functions.put(new LispAtom("cdr"), LispList::cdr);
		functions.put(new LispAtom("car"), LispList::car);

	}
	
	/**
	 * getFunction - if la is valid function return the corresponding LispFunction else null
	 * @param la
	 */
	LispFunction getFunction(LispAtom la) {
		LispFunction lf;
		
		lf = functions.get(la);
		return lf;
	}
	
	private void setAsNumber() {
		try {
			   atomAsNumber = new BigDecimal(this.atom);
			}
			catch (NumberFormatException e)
			{
			   atomAsNumber = new BigDecimal("0");
			}	
	}
	public LispAtom() {
		atom = new String();
		atomAsNumber = new BigDecimal("0");
	}
	public LispAtom(String s) {
		atom = s;
		setAsNumber();
	}
	public LispAtom(BigDecimal num) {
		atomAsNumber = num;
		atom = num.toString();
	}
	@Override
	public boolean isList() {
		return false;
	}
	
	@Override
	public boolean isAtom() {
		return true;
	}
	
	@Override
	public int length() {
		return atom.length();
	}
	@Override
	public boolean isEmpty() {
		return false;
	}
    @Override
    public boolean equals(Object obj) 
    { 
    	if(this == obj) 
            return true; 
          
        if(obj == null || obj.getClass()!= this.getClass()) 
            return false; 
          
        // type casting of the argument.  
        LispAtom la = (LispAtom) obj; 
          
        return la.atom.equals(this.atom);
    } 
    @Override
    public int hashCode() 
    { 
    	return atom.hashCode();  //Uses String's hashCode default implementation
    }
	@Override
	public String toString() {
		return atom;
	}
	
	public int intValue() {
		int retval;
		retval = atomAsNumber.toBigInteger().intValueExact();
		return retval;
	}
	public static LispAtom downcast(LispList ll) {
		if (ll instanceof LispAtom) {
			return (LispAtom) ll;
		}
		return null;
	}

	
	/**
	 * 
	 * 
	 * @param la
	 * @return
	 */
	public LispAtom sumAtom(LispAtom la) {
			//logger.debug("In LispAtom::add method"); // To double check function dereferencing is working
			
			LispAtom result;
			BigDecimal calc = new BigDecimal("0");
			calc = calc.add(this.atomAsNumber);
			calc = calc.add(la.atomAsNumber);
			result = new LispAtom(calc);
			return result;    
			  
	} 

	
	public boolean isEvenlyDivisible(int mod) {
		   boolean ret;
		   BigInteger bi;
		   
		    try {
		        bi = atomAsNumber.toBigIntegerExact();
		    } catch (ArithmeticException ex) {
		        ret = false;
		        return ret;
		    }

		    ret = bi.mod(BigInteger.valueOf(mod)).compareTo(BigInteger.valueOf(0)) == 0;
		    return ret;
	}
	

	public void test() {
		boolean result;
		initializeFunctions();
		
		LispAtom la = new LispAtom("9");
		LispModFunction func = LispAtom::isEvenlyDivisible;
		
		result = func.checkMod(la, 3);
		System.out.println(result);
		
		
	}
	public void test2() {
		LispList result;
		LispAtom la = new LispAtom("88");
		LispAtom lb = new LispAtom("1");

		//LispOldFunction func = functions.get(new LispAtom("+"));
		LispOldFunction func = LispAtom::sumAtom;
		
		result = func.checkMod(la, lb);
		System.out.println("Result is: ");
		System.out.println(result.toString()); 
	}
	public void test3() {
		LispList result;
		
		LispFunction func = functions.get(new LispAtom("+"));
		//LispFunction func = LispAtom::addFlatList;

		LispAtom la = new LispAtom("4");
		LispAtom lb = new LispAtom("7");
		LispAtom lc = new LispAtom("13");
		
		LispList ll = LispList.list(la, lb, lc);
		result = func.checkMod(la, ll);
		System.out.println("Result is: ");
		System.out.println(result.toString()); 

	}
	public static void main(String[] args) {
		LispAtom test = new LispAtom();
		
		test.test3();
	}
	
	
	@Override
	/**
	 * eval of an atom is itself
	 */
	public LispList eval() {
		logger.debug("Eval(" + this + ")");
		return this;
	}
	
	/**
	 * Following are unsupported operations on LispAtom
	 * 
	 * */

	@Override
	public LispList car() {
		throw new UnsupportedOperationException();
	}
	@Override
	public LispList cdr() {
		throw new UnsupportedOperationException();
	}
}
