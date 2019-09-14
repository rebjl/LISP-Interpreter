
@FunctionalInterface
public interface LispFunction {
	//classInstance is use to deference into the correct object
	 LispList checkMod(LispList classReference , LispList arg);
}
