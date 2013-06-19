package as.jde.main.input;

import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;

public abstract class AbstractArgument implements Argument {

	private Entry<String, String> argument;
			
	protected AbstractArgument(String key, String value) {
		 argument = new SimpleEntry<String,String>(key, value);
	}
	
	public void parseArgument(String argument, String argument_flag) {
		if (argument.startsWith(argument_flag)) {
			String value = argument.substring(argument.length()+1);
			this.argument.setValue(value);
		}
	}

	public void parseArgument(String input_flag, String value, String argument_flag) {
		if (input_flag.equals(argument_flag)) {
			argument.setValue(value);
		}	
	}

	@Override
	public Entry<String, String> getArgument() {
		return argument;
	}
}
