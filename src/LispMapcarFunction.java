@FunctionalInterface
public interface LispMapcarFunction {
	
	//The type of arg has to match exactly in the class instance 
	LispList map(LispList classInstance) throws Exception;
		

}
