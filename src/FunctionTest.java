
import java.math.BigDecimal;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Test;

import junit.framework.TestCase;


/**
 * Some simple tests.
 *
 */
public class FunctionTest extends TestCase {
	protected int fValue1;
	protected int fValue2;

	static final Logger logger = Logger.getLogger(FunctionTest.class);

	static {
		logger.setLevel(Level.INFO); 
	}
	
	@Override
	protected void setUp() {
		fValue1= 2;
		fValue2= 3;
	}

	@Test
	public void testAdd() {
		BigDecimal arg1 = new BigDecimal("2");
		BigDecimal arg2 = new BigDecimal("3");
		LispList res = LispList.emptyList(), expectedRes = LispList.emptyList();
		
		LispList ll;
		try {
			ll = ReadLispList.strToList("(+ 2 3)");
			
			expectedRes = new LispAtom("5");
			res = ll.eval();
		}
		 catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
		assertTrue(res.equals(expectedRes));
	}
	@Test
	public void testCar() {
		LispList res = LispList.emptyList(), expectedRes = LispList.emptyList();
		
		LispList ll;
		try {
			ll = ReadLispList.strToList("(car '(a b c))");
			
			expectedRes = new LispAtom("a");
			res = ll.eval();
		}
		 catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
		assertTrue(res.equals(expectedRes));
	}
	@Test
	public void testCdr() {
		LispList res = LispList.emptyList(), expectedRes = LispList.emptyList();
		
		LispList ll;
		try {
			ll = ReadLispList.strToList("(cdr '(a b c))");
			
			expectedRes = ReadLispList.strToList("(b c)");
			res = ll.eval();
		}
		 catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
		assertTrue(res.equals(expectedRes));
	}
	@Test
	public void testQuote() {
		LispList res = LispList.emptyList(), expectedRes = LispList.emptyList();
		
		LispList ll;
		try {
			ll = ReadLispList.strToList("'(a b c)");
			
			expectedRes = ReadLispList.strToList("(a b c)");
			res = ll.eval();
		}
		 catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
		assertTrue(res.equals(expectedRes));
	}
	@Test
	public void testAdditionWithQuotes() {
		LispList res = LispList.emptyList(), expectedRes = LispList.emptyList();
		
		LispList ll;
		try {
			ll = ReadLispList.strToList("(+ '2 '4 5)");
			
			expectedRes = ReadLispList.strToList("11");
			res = ll.eval();
		}
		 catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
		assertTrue(res.equals(expectedRes));
	}
	@Test
	public void testCarWithCdr() {
		LispList res = LispList.emptyList(), expectedRes = LispList.emptyList();
		
		LispList ll;
		try {
			ll = ReadLispList.strToList("(car (cdr '(a b c)))");
			
			expectedRes = ReadLispList.strToList("b");
			res = ll.eval();
		}
		 catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
		assertTrue(res.equals(expectedRes));
	}
	@Test
	public void testCdrWithCar() {
		LispList res = LispList.emptyList(), expectedRes = LispList.emptyList();
		
		LispList ll;
		try {
			ll = ReadLispList.strToList("(cdr (car '((a b) c d)))");
			
			expectedRes = ReadLispList.strToList("(b)");
			res = ll.eval();
		}
		 catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
		assertTrue(res.equals(expectedRes));
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void testExceptionOnCdrOfAtom() throws Exception {
		LispList res = LispList.emptyList();
		
		LispList ll;
		try {
			ll = ReadLispList.strToList("(cdr a)");
			res = ll.eval();
		}
		 catch (Exception e) {
			 logger.info("In testExceptionOnCdrOfAtom: " + e);
		} 
	}
	@Test(expected = UnsupportedOperationException.class)
	public void testExceptionOnCarOfAtom() throws Exception {
		LispList res = LispList.emptyList();
		
		LispList ll;
		try {
			ll = ReadLispList.strToList("(car a)");
			res = ll.eval();
		}		
		 catch (Exception e) {
			 	logger.info("In testExceptionOnCarOfAtom: " + e);
		} 
	}
	public static void main (String[] args) {
		//junit.textui.TestRunner.run(suite());
	}
}