package as.jde.main.input;

public class IgnoreFolderArgument extends AbstractArgument {

	public static final String OPT_IGNORE_FOLDER = "--ignore";
	public static final String OPT_IGNORE_FOLDER_SHORT = "-i";
	public static final String IGNORE_FOLDER_DEFAULT = "\\b\\B";
	public static final String DEFAULT = IGNORE_FOLDER_DEFAULT;
	
	public IgnoreFolderArgument() {
		super(OPT_IGNORE_FOLDER, DEFAULT);
	}

	@Override
	public void parseArgument(String argument) {
		super.parseArgument(argument, OPT_IGNORE_FOLDER);
	}

	@Override
	public void parseArgument(String flag, String value) {
		super.parseArgument(flag, value, OPT_IGNORE_FOLDER_SHORT);
	}

	@Override
	public String getHelpMessage() {
		return OPT_IGNORE_FOLDER_SHORT
				+ " regexp | "
				+ OPT_IGNORE_FOLDER
				+ "=regexp\n\t\tspecified regular expression of foldernames that should be ignored.";
	}
}
