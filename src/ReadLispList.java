import java.util.ArrayList;
import java.util.Scanner;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class ReadLispList {
	static final Logger logger = Logger.getLogger(ReadLispList.class);

	static {
		logger.setLevel(Level.DEBUG); 
	}
	
	static public final String WITH_DELIMITER = "((?<=%1$s)|(?=%1$s))";
	static public final String DELIMS = "[\\s,(,),\\']";
	static public final String PROGRAM_NAME = "Mini LISP";
	static public final String CONSOLE_PROMPT = PROGRAM_NAME + "> ";
	
	public int skipWhiteSpaceTokens(String[] tokens, int offset) {
		while (tokens[offset].matches("\\s")) {
			offset++;
		}
		return offset;
	}
	
	public static ArrayList<String> removeWhiteSpaceTokens(String[] tokens) {
		ArrayList<String> newTokens = new ArrayList<String>();
		
		for (int i = 0; i < tokens.length; i++) {
			if (tokens[i].matches("\\s")) {
				continue;
			}
			newTokens.add(tokens[i]);
		}

		return newTokens;
	}
	/**
	 * Checks that there are equal numbers of open & close parenthesis & no close before a corresponding open
	 * @param tokens
	 * @return
	 */
	public static boolean checkValidParentheses(ArrayList<String> tokens) {
		int numOpen = 0;
		
		for (String s : tokens) {
			if (s.matches("\\(")) {
				numOpen++;
			}
			else if (s.matches("\\)")) {
				if (numOpen <= 0)
					return false;
				numOpen--;
			}	
		}
		return (numOpen == 0);
	}
	/** 
	 * Returns the offset of the closing parenthesis for the s-exp starting at startOffset
	 * Or return -1, if syntax error
	 * 
	 * @param tokens
	 * @param offset
	 * @return
	 */
	public static int findInnerSExp(ArrayList<String> tokens, int startOffset) {
		int numOpen = 0;
		String s;
		
		for (int endOffset = startOffset; endOffset < tokens.size(); endOffset++) {
			s = tokens.get(endOffset);
			
			if (s.matches("\\(")) {
				numOpen++;
			}
			else if (s.matches("\\)")) {
				if (numOpen <= 0)
					return -1;
				numOpen--;
			}	
			if (numOpen == 0)
				return endOffset;
		}
		return -1;
	}
	
	/**
	 * Expands the single quote shortcut (') into (quote ... )
	 * Note: does not handle nested single quotes within quotes
	 * 
	 * @param tokens
	 * @return
	 * @throws Exception
	 */
	public static ArrayList<String> expandQuotes(ArrayList<String> tokens) throws Exception {
		ArrayList<String> newTokens = new ArrayList<String>();
		int startOffset, endOffset;
		int j;
		
		for (int i = 0; i < tokens.size(); i++) {
			if (tokens.get(i).matches("'")) {
				startOffset = i+1;
				// The case where an atom is quoted
				if (!tokens.get(startOffset).matches("\\(")) {
					endOffset = startOffset;
				}
				else {
					endOffset = findInnerSExp(tokens, startOffset);
					if (endOffset == -1)
						throw new Exception("Parentheses in expression don't match");
				}
				
				newTokens.add("(");
				newTokens.add("quote");
				
				for (j = startOffset; j <= endOffset; j++) {
					newTokens.add(tokens.get(j));
				}
				
				newTokens.add(")");
				i = j - 1;   //i will immediately increment by 1 in the for loop
			}
			else
				newTokens.add(tokens.get(i));
		}

		return newTokens;	
	}
	
	// Recursive function to make nested lists in ll.  Returns the offset of the next un-consumed token
	public static int newListRecursive(LispList ll, ArrayList<String> tokens, int offset) {
	
		while (offset < tokens.size()) {
			 // Start of nested list. Create new list add it to current LispList
			 if (tokens.get(offset).matches("\\(")) { 
				 offset++;
				 LispList innerList = new LispList();
				 ll.add(innerList);
				 offset = newListRecursive(innerList, tokens, offset);
			 }
			 else if (tokens.get(offset).matches("\\)")) {
				 offset++;
				 return offset;
			 }
			 //Assumed atom
			 else {
				 LispAtom la = new LispAtom(tokens.get(offset));
				 offset++;
				 ll.add(la);
			 }
		}
		return offset;
	}
	public static LispList strToList(String s) throws Exception {
		 String reg = String.format(WITH_DELIMITER, DELIMS);
		 String[] tempTokens = s.split(reg);
		 ArrayList<String> newTokens;
		 LispList retval;
		 
		 newTokens = removeWhiteSpaceTokens(tempTokens);
		 
		 if (!checkValidParentheses(newTokens)) {
			 throw new Exception("Parentheses in expression don't match");
		 }
		 newTokens = expandQuotes(newTokens);
		 logger.debug("newTokens after expansion: " + newTokens);
		 
		 // Check if this is an atom
		 if (!newTokens.get(0).matches("\\(")) { 
			 if (newTokens.size() > 1) {
				 throw new Exception("Expecting single atom but got more tokens");
			 }
			 retval = new LispAtom(newTokens.get(0));
			 return retval;
		 }
		 // Make a list & consume the first open parenthesis
		 retval = new LispList();
		 newListRecursive(retval, newTokens, 1);
		 
		 return retval;
	}

	public void printHelp() {
		String validCmds = "quit + ";
		String msg = "Welcome to " + PROGRAM_NAME + "!\n\n" +
					"Valid commands are: " + validCmds ;
					//"\n\nTo exit type: exit";
		System.out.println(msg);
	}
	public void readEvalLoop() throws Exception {
		//String currLine = " (1 (2 (4 5)) 3)";
		//String currLine = " ( )";
		
		// Using Scanner for Getting Input from User 
        Scanner in = new Scanner(System.in); 
        LispList result;
        
        printHelp();
        while (true) {
        	System.out.print(CONSOLE_PROMPT);
	        String currLine = in.nextLine(); 
	        
	        result = LispList.emptyList();
	        
	        try {
		        currLine = currLine.trim();
		        if (currLine.equalsIgnoreCase("quit"))
		        		break;
				LispList ll =  strToList(currLine);
				logger.debug("Evaluating: " + ll.toString());
				
				result = ll.eval();
	        }
			catch (Exception e) {
				System.out.println(e);
				e.printStackTrace();
			}
			System.out.println(result);
        }
		in.close();
	}
	public static void main(String[] args) throws Exception {
		ReadLispList test = new ReadLispList();
		
		test.readEvalLoop();
	
	}
	public static void mainOld(String[] args) {
		// TODO Auto-generated method stub
		 String currLine = " ( 1 2 3)";
		 
		 String reg = String.format(WITH_DELIMITER, DELIMS);
		 
		 System.out.println("Regex is:" + reg);
		 
		  String[] aEach = currLine.split(reg);
		  
		  for (String s : aEach) {
			  System.out.println(s);
		  }
		  System.out.println("****");
		  for (String s : aEach) {
			  System.out.print(s);
		  }
		  System.out.println("");
	};
	

}
