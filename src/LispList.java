import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Function;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class LispList implements Iterable<LispList>{
	private ArrayList<LispList> list;
	
	static final Logger logger = Logger.getLogger(LispList.class);

	static {
		logger.setLevel(Level.DEBUG); 
	}
	
	@Override
	public Iterator<LispList> iterator() {
	        return list.iterator();
	}
	
    @Override
    public boolean equals(Object obj) 
    { 
        boolean retval = true;
        
    	if(this == obj) 
            return true; 
          
        if(obj == null || obj.getClass()!= this.getClass()) 
            return false; 
          
        // type casting of the argument.  
        LispList ll = (LispList) obj; 
          
        if (this.length() != ll.length())
        	return false;
        for (int i = 0; i < this.length(); i++) {
        	if (!this.list.get(i).equals(ll.list.get(i))) {
        		return false;
        	}
        }
        return false;
    } 
    
	public LispList add(LispList a) {
		list.add(a);
		return this;
	}	
	public LispList addAll(LispList a) {
		list.addAll(a.list);
		return this;
	}
	public LispList() {
		list = new ArrayList<LispList>();
	}
	
	public boolean isList() {
		return true;
	}
	public boolean isAtom() {
		return false;
	}
	
	public static LispList cons(LispList a, LispList b) {
		LispList ll = new LispList();
		
		ll.list.add(a);
		for (LispList tempLL : b) {
			ll.list.add(tempLL);
		}
		return ll;
	}
	
	public static LispList emptyList() {
		return new LispList();
	}
	// Variable number of args
	public static LispList list(LispList ...args) {
		LispList ll = new LispList();
		
		for (LispList tempLL : args)
			ll.list.add(tempLL);
		return ll;
	}

	// Variable number of args must be at least 2
	//TODO this is buggy!!
	public static LispList append(LispList ...args) {
		if (args.length < 2) {
			throw new RuntimeException("Too few arguments to append");
		}
		LispList ll = new LispList();
		ll.addAll(args[0]);
		for (int i = 1; i < args.length; i++)
			ll.addAll(args[i]);
		return ll;
	}
	// In place concatenate
	public static LispList nconc(LispList ...args) {
		if (args.length < 2) {
			throw new RuntimeException("Too few arguments to nconc");
		}

		for (int i = 1; i < args.length; i++) {
			for (LispList tempLL : args[i])
				args[0].add(tempLL);
		}
		return args[0];
	}	
	public static int length(LispList ll) {
		return ll.list.size();
	}
	public int length() {
		return list.size();
	}
	public boolean isEmpty() {
		return list.size() == 0;
	}
	private static LispList car(LispList ll) {
		if (ll.list.size() == 0)
			return LispList.emptyList();
		return ll.list.get(0);
	}
	public LispList car() {
		return LispList.car(this);
	}
	/*
		+ (LispList*) cdr:(LispList*) l   
	{
		LispList* retval;
		retval = [LispList newInstance];
		if ([l length] <=1) {
			return [retval autorelease];
		}
		for (int i = 1; i < [l length]; i++) {
			[retval.list addObject:[l.list objectAtIndex:i]];
		}
		return [retval autorelease];
	} */
	public static LispList cdr(LispList ll) {
		LispList retval = new LispList();
		
		if (ll.length() <= 1)
			return retval;
		for (int i = 1; i < ll.length(); i++) {
			retval.add(ll.list.get(i));
		}
		return retval;
	}
	public LispList cdr() {
		return LispList.cdr(this);
	}
	/*
		  + (LispList*) caar:(LispList*) l
	{
		LispList* retval;
		retval = [LispList car:l];
		if ([retval length] == 0) {
			return [LispList emptyList];
		}
		else {
			return [LispList car:retval];
		} 
	} */
	 
	public static LispList caar(LispList ll) {
		 LispList retval;
		 
		 retval = ll.car();
		 if (retval.length() == 0)
			 return LispList.emptyList();
		 return retval.car();
	}
	public LispList caar() {
		return LispList.caar(this);
	}
	
	/* + (LispList*) cddr:(LispList*) l
{
	LispList* retval;
	retval = [LispList cdr:l];
	if ([retval length] == 0) {
		return [LispList emptyList];
	}
	else {
		return [LispList cdr:retval];
	} 
}	*/
	public static LispList cddr(LispList ll) {
		 LispList retval;
		 retval = LispList.cdr(ll);
		 if (retval.length() == 0)
			 return LispList.emptyList();
		 return LispList.cdr(retval);
	}
	public LispList cddr() {
		return LispList.cddr(this);
	}
	/*+ (LispList*) cdar:(LispList*) l
	{
		LispList* retval;
		retval = [LispList car:l];
		if ([retval length] == 0) {
			return [LispList emptyList];
		}
		else {
			return [LispList cdr:retval];
		} 
	} */
	public static LispList cdar(LispList ll) {
		 LispList retval;
		 retval = LispList.car(ll);
		 if (retval.length() == 0)
			 return LispList.emptyList();
		 return LispList.cdr(retval);
	}
	public LispList cda() {
		return LispList.cdar(this);
	}
	/* + (LispList*) cadr:(LispList*) l
	{
		LispList* retval;
		
		retval = [l elt:1];
		return retval;
		 */
	
	static LispList cadr(LispList ll) {
		return ll.elt(1);
	}
	
	LispList cadr() {
		return cadr(this);
	}
	/*
	- (LispList*) elt:(NSInteger) index
	{
		if (index >= 0 && index < [list count]) 
			return [list objectAtIndex:index];
		else 
			return [LispList emptyList];
	} */
	//TODO throw exception if index out of bounds
	public LispList elt(int index) {
		if (index >= 0 && index < list.size())
			return list.get(index);
		return LispList.emptyList();
	}
	/*
	 + (LispList*) cadar:(LispList*) l
{
	LispList* retval;
	retval = [LispList cdar:l];
	if ([retval length] == 0) {
		return [LispList emptyList];
	}
	else {
		return [LispList car:retval];
	} 
}
	 */
	static LispList cadar(LispList ll) {
		 LispList retval;
		 retval = LispList.cdar(ll);
		 if (retval.length() == 0)
			 return LispList.emptyList();
		 return LispList.car(retval);
	}
	LispList cadar() {
		return LispList.cadar(this);
	}
	/* 
		 * + (LispList*) caddr:(LispList*) l
	{
		LispList* retval;
		retval = [LispList cddr:l];
		if ([retval length] == 0) {
			return [LispList emptyList];
		}
		else {
			return [LispList car:retval];
		} 
	}	*/
	 
	static LispList caddr(LispList ll) {
		 LispList retval;
		 retval = LispList.cddr(ll);
		 if (retval.length() == 0)
			 return LispList.emptyList();
		 return LispList.car(retval);
	}
	LispList caddr() {
		return LispList.caddr(this);
	}
	/* + (LispList*) caadr:(LispList*) l
	{
		LispList* retval;
		retval = [LispList cadr:l];
		if ([retval length] == 0) {
			return [LispList emptyList];
		}
		else {
			return [LispList car:retval];
		} 
	} */
	static LispList caadr(LispList ll) {
		 LispList retval;

		 retval = LispList.cadr(ll);
		 if (retval.length() == 0)
			 return LispList.emptyList();
		 return LispList.car(retval);
	}
	LispList caadr() {
		return LispList.caadr(this);
	}
	
	static LispList reverse(LispList ll) {
		LispList retval = new LispList();
		for (int i = ll.length()-1; i >= 0; i--) {
			retval.add(ll.elt(i));
		}
		return retval;
	}
	
	/** Return the nth element of list or empty list if it doesn't exist
	 * 
	 * @param n
	 * @return
	 */
	LispList nth(int n) {
		if (n >= 0 && n < list.size())
			return list.get(n);
		return emptyList();
	}
	 /*
		  * + (LispList*) mapCar:(LispList*) inList and: (MapCarFunc*) func
	{
		LispList* retval;
		retval = [LispList newInstance];
		for (LispList* ll in inList.list) {
			[retval.list addObject:[func apply:ll]];
		}
		return [retval autorelease];
	}
	  */
	@Deprecated
	static LispList mapCarDeprecated(LispList ll, Function<LispList, LispList> func ) {
		LispList retval = new LispList();
		
		for (LispList tempLL : ll) {
			retval.add(func.apply(tempLL));
		}	
		return retval;
	 }
	static LispList mapCar(LispList ll, LispMapcarFunction func ) throws Exception {
		LispList retval = new LispList();
		
		for (LispList tempLL : ll) {
			retval.add(func.map(tempLL));
		}	
		return retval;
	 }
	
	public LispList quote(LispList ll) {
		return ll;
	}
	/* - (NSString*) toString
	{
		NSMutableString* ms = [[NSMutableString alloc] initWithString:@"("];
		NSString *retval;  // = [[NSString alloc] initWithString:@""];
		
		for (LispList* ll in list) {
			[ms appendString:[ll toString]];
			[ms appendString:@" "];
		}
		[ms appendString:@")"]; 
		retval = [ms copy];
		[ms release];
		return [retval autorelease];
	} */
	public String toString() {
		StringBuffer retval = new StringBuffer();
		
		retval.append("(");
		for (LispList ll : list) {
			String inner = ll.toString();
			
			// Only prefix a space if previous char is not an open parenthesis
			if (!retval.toString().endsWith("("))
			 retval.append(" ");	
			
			retval.append(inner);
			

		}
		retval.append(")");
		return retval.toString();
	}
	
	/**
	 * ll is assumed to be a flat list of atoms
	 * TODO should throw an exception if ll is not a flat list of atoms
	 * @param ll
	 * @return
	 */
	public LispAtom sumFlatList(LispList ll) {
		LispAtom result = new LispAtom(new BigDecimal("0"));
		
		//logger.debug("In LispAtom::addNew");
		for (int i = 0; i < ll.length(); i++) {
			LispAtom la = LispAtom.downcast(ll.nth(i));
			result = result.sumAtom(la);
			logger.debug("ListAtom::addNew Intermediate result: " + result);
		}
		return result;  
	}
	

	public LispList eval() throws Exception {

		LispAtom la;
		LispList car;
		LispFunction lf;
		
		car = this.car();
		
		if (car.isList()) {
			throw new Exception("List is not a function: " + car);
		}
		la = (LispAtom) car;
		
		lf = la.getFunction(la);

		if (lf == null) {
			throw new Exception("Atom is not a function: " + la);
		}
		logger.debug("Function is: " + la);
		
		LispList result = LispList.emptyList();
		//TODO check that quote only has one arg. If not it's probably an error
		if (la.equals(new LispAtom("quote"))) {
			result = lf.checkMod(this.cadr(), this.cadr());
			logger.debug("Result is: " + result);
			return result;
		}
		
		//Evaluate each element in the cdr creating a new list of the results
		LispList args;
		
		LispMapcarFunction func = LispList::eval;
		args = LispList.mapCar(this.cdr(), func);
		
		logger.debug("Argument eval is: " + args);
		
		LispAtom arg1 = LispAtom.downcast(args.car());
		
		//The first argument is pretty much irrelevant. I think I should use the function atom
		 result = lf.checkMod(arg1, args); 
		
		logger.debug("Result is: " + result);
		return result;

	}
}
