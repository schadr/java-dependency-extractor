package as.jcge.main;

import java.util.HashMap;
import java.util.Map;

public class ArgumentParser {
	public static final String OPT_REPOSITORY = "--repository";
	
	public static void printHelp() {
		System.out.println("./extract.sh [options] pathToRepository");
		System.out.println("options:");
		System.out.println("\t-r type | --repository=type\n\t\tsepcifies the type of repository the source is contained, currently supported repos are git (default)");
		System.out.println("\t-f type | --output-format=type\n\t\tspecifies the format of the output, currently supported are xml (default)");
	}
	
	public static String cleanArgument(String arg, String[] opts) {
		String ret = arg;
		for (String opt : opts) {
			if (ret.startsWith(opt)) ret = ret.replaceFirst(opt, "");
		}
		
		return ret;
	}
	
	public static Map<String, String> parseArguments(String[] args) {
		Map<String,String> optArgs = new HashMap<String,String>();
		String arg;
		boolean okArg = false;
		for (int i = 0; i < args.length; i++) {
			okArg = false;
			arg = args[i];
			if (arg.startsWith("-r") || arg.startsWith("--repository")) {
				okArg=true;
				String value = cleanArgument(arg,new String[] {"-r","--repository"}); 
				if (value.startsWith("=")) {
					value.replaceFirst("=", "");
					if (value.length() == 0) {
						System.out.println(arg + " needs to be followed by a value without leading whitespace\n");
						printHelp();
						System.exit(0);
					} else {
						optArgs.put(OPT_REPOSITORY, value);
					}
				} else {
					i++;
					arg = args[i];
					if (arg.startsWith("-")) {
						System.out.println(args[i-1] + " flag needs to be provided with a value\n");
						printHelp();
						System.exit(0);
					} else {
						optArgs.put(OPT_REPOSITORY, arg);
					}
				}
			}
			if (arg.startsWith("-f") || arg.startsWith("--output=format")) {
				okArg=true;
			}
			if (!okArg) {
				System.out.println("Argument " + arg + " not recognized\n");
				printHelp();
				System.exit(0);
			}
		}
		
		return optArgs;
	}
}
