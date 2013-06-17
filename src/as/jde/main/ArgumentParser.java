package as.jde.main;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ArgumentParser {
	public static final String OPT_REPOSITORY = "--repository";
	public static final String OPT_REPOSITORY_SHORT = "-r";
	public static final String REPOSITORY_GIT = "git";
	
	public static final String OPT_OUTPUT_FORMAT = "--output-format";
	public static final String OPT_OUTPUT_FORMAT_SHORT = "-f";
	public static final String OUTPUT_XML = "xml";
	
	public static final String OPT_IGNORE_FOLDER = "--ignore";
	public static final String OPT_IGNORE_FOLDER_SHORT = "-i";
	public static final String IGNORE_FOLDER_DEFAULT = "\\b\\B";
	
	public static final String OPT_QUEUE_LIMIT = "--queue-size";
	public static final String OPT_QUEUE_LIMIT_SHORT = "-q";
	public static final String QUEUE_LIMIT_DEFAULT = "10";
	
	public static final String OPT_TMP_FOLDER = "--folder";
	public static final String OPT_TMP_FOLDER_SHORT = "-f";
	
	public static final String OPT_TMP_COPIES = "--num-copies";
	public static final String OPT_TMP_COPIES_SHORT = "-n";
	public static final String TMP_COPIES_DEFAULT = "1";
	

	public static final String OPT_REPOSITORY_LOCATION = "repository location";
	
	public static void printHelp() {
		System.out.println("./extract.sh [options] pathToRepository");
		System.out.println("options:");
		System.out.println("\t"+OPT_REPOSITORY_SHORT+" type | "+OPT_REPOSITORY+"=type\n\t\tsepcifies the type of repository the source is contained, currently supported repos are "+REPOSITORY_GIT+" (default)");
		System.out.println("\t"+OPT_OUTPUT_FORMAT_SHORT+" type | "+OPT_OUTPUT_FORMAT+"=type\n\t\tspecifies the format of the output, currently supported are "+OUTPUT_XML+" (default)");
		System.out.println("\t"+OPT_IGNORE_FOLDER_SHORT+" regexp | "+OPT_IGNORE_FOLDER+"=regexp\n\t\tspecified regular expression of foldernames that should be ignored.");
		System.out.println("\t"+OPT_QUEUE_LIMIT_SHORT+" size | "+OPT_QUEUE_LIMIT+"=size\n\t\tspecifies the size of the output queue (default: "+QUEUE_LIMIT_DEFAULT+") (-1: no limit)");
		System.out.println("\t"+OPT_TMP_FOLDER_SHORT+" foldername | "+OPT_TMP_FOLDER+"=foldername\n\t\tspecifies a folder that the current user can write to to created multiple copies of the repository"
							+"\n\t\tnote that by default the git repo specified will be used to check out all versions, even if a tmp folder is specified "
							+"\n\t\tas long as the number of copies is not greater than 1.");
		System.out.println("\t"+OPT_TMP_COPIES_SHORT+" number_of_copies | "+OPT_TMP_COPIES+"=number_of_copies\n\t\tspecifies the number of copies of repo made, if a folder for the copies was specified (default=1).");
	}
	
	public static Map<String, String> parseArguments(String[] args) {
		Map<String,String> optArgs = createDefaultOptions();
		String arg;
		boolean okArg = false;
		
		for (int i = 0; i < args.length; i++) {
			okArg = false;
			arg = args[i];
			// parsing repository information
			if (arg.startsWith(OPT_REPOSITORY_SHORT) || arg.startsWith(OPT_REPOSITORY)) {
				okArg=true;
				String[] flags = {OPT_REPOSITORY, OPT_REPOSITORY_SHORT};
				String[] legitValues = {REPOSITORY_GIT};
				if (!hasValue(optArgs, arg, flags, OPT_REPOSITORY, legitValues)) {
					addNextValue(args, optArgs, i++, OPT_REPOSITORY, legitValues);
				}
			}
			// parsing output format information
			if (arg.startsWith(OPT_OUTPUT_FORMAT_SHORT) || arg.startsWith(OPT_OUTPUT_FORMAT)) {
				okArg=true;
				String[] flags = {OPT_OUTPUT_FORMAT, OPT_OUTPUT_FORMAT_SHORT};
				String[] legitValues = {OUTPUT_XML};
				if (!hasValue(optArgs, arg, flags, OPT_OUTPUT_FORMAT, legitValues)) {
					addNextValue(args, optArgs, i++, OPT_OUTPUT_FORMAT, legitValues);
				}
			}
			if (arg.startsWith(OPT_IGNORE_FOLDER_SHORT) || arg.startsWith(OPT_IGNORE_FOLDER)) {
				okArg=true;
				String[] flags = {OPT_IGNORE_FOLDER, OPT_IGNORE_FOLDER_SHORT};
				if (!hasValue(optArgs, arg, flags, OPT_IGNORE_FOLDER, null)) {
					addNextValue(args, optArgs, i++, OPT_IGNORE_FOLDER, null);
				}
			}
			if (arg.startsWith(OPT_QUEUE_LIMIT_SHORT) || arg.startsWith(OPT_QUEUE_LIMIT)) {
				okArg=true;
				String[] flags = {OPT_QUEUE_LIMIT, OPT_QUEUE_LIMIT_SHORT};
				if (!hasValue(optArgs, arg, flags, OPT_QUEUE_LIMIT, null)) {
					addNextValue(args, optArgs, i++, OPT_QUEUE_LIMIT, null);
				}
			}
			if (arg.startsWith(OPT_TMP_COPIES) || arg.startsWith(OPT_TMP_COPIES_SHORT)) {
				okArg=true;
				String[] flags = {OPT_TMP_COPIES, OPT_TMP_COPIES_SHORT};
				if(!hasValue(optArgs, arg, flags, OPT_TMP_COPIES, null)) {
					addNextValue(args, optArgs, i++, OPT_TMP_COPIES, null);
				}
			}
			if (arg.startsWith(OPT_TMP_FOLDER) || arg.startsWith(OPT_TMP_FOLDER_SHORT)) {
				okArg=true;
				String[] flags = {OPT_TMP_FOLDER, OPT_TMP_FOLDER_SHORT};
				if(!hasValue(optArgs, arg, flags, OPT_TMP_FOLDER, null)) {
					addNextValue(args, optArgs, i++, OPT_TMP_FOLDER, null);
				}
			}
			if (!okArg) {
				if (i == args.length - 1) {
					if (!args[i].endsWith("/")) args[i] = args[i] + File.separator;
					optArgs.put(OPT_REPOSITORY_LOCATION, args[i]);
				} else {
					System.out.println("Argument " + arg + " not recognized\n");
					printHelp();
					System.exit(0);
				}
			}
		}
		
		return optArgs;
	}

	private static Map<String, String> createDefaultOptions() {
		Map<String,String> optArgs = new HashMap<String,String>();
				
		optArgs.put(OPT_OUTPUT_FORMAT, OUTPUT_XML);
		optArgs.put(OPT_REPOSITORY, REPOSITORY_GIT);
		optArgs.put(OPT_QUEUE_LIMIT, QUEUE_LIMIT_DEFAULT);
		optArgs.put(OPT_IGNORE_FOLDER, IGNORE_FOLDER_DEFAULT);
		optArgs.put(OPT_TMP_COPIES, TMP_COPIES_DEFAULT);
				
		return optArgs;
	}

	private static void addNextValue(String[] args, Map<String, String> optArgs, int i, String optionFlag, String[] legitValues) {
		String arg;
		arg = args[i+1];
		if (arg.startsWith("-")) {
			System.out.println(args[i] + " flag needs to be provided with a value\n");
			printHelp();
			System.exit(0);
		} else {
			optArgs.put(optionFlag, arg);
		}
	}

	private static boolean hasValue(Map<String, String> optArgs, String arg, String[] optFlags, String optionFlag, String[] legitValues) {
		String value = cleanArgument(arg, optFlags);
		value = value.replaceFirst("=", "");
		if (value.length() == 0) {
			System.out.println(arg + " needs to be followed by a value without leading whitespace\n");
			printHelp();
			System.exit(0);
		} else {
			optArgs.put(optionFlag, value);
			return true;
		}
		return false;
	}
	
	private static String cleanArgument(String arg, String[] opts) {
		String ret = arg;
		for (String opt : opts) {
			if (ret.startsWith(opt)) ret = ret.replaceFirst(opt, "");
		}
		
		return ret;
	}
}
