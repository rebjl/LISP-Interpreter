
@FunctionalInterface
public interface LispOldFunction {
	
	//The type of arg has to match exactly in the class instance 
	LispList checkMod(LispAtom classInstance, LispAtom arg);

}

