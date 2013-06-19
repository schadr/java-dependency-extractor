package as.jde.main.input;

import java.util.Map.Entry;

public interface Argument {
	void parseArgument(String argument);
	void parseArgument(String flag, String value);
	Entry<String, String> getArgument();
	String getHelpMessage();
}