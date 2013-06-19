package as.jde.main.input;


public class TempFolderArgument extends AbstractArgument {
	
	public static final String OPT_TMP_FOLDER = "--folder";
	public static final String OPT_TMP_FOLDER_SHORT = "-f";
	public static final String DEFAULT = "tmp";

	public TempFolderArgument() {
		super(OPT_TMP_FOLDER, DEFAULT);
	}

	@Override
	public void parseArgument(String argument) {
		super.parseArgument(argument, OPT_TMP_FOLDER);
	}

	@Override
	public void parseArgument(String flag, String value) {
		super.parseArgument(flag, value, OPT_TMP_FOLDER_SHORT);
	}

	@Override
	public String getHelpMessage() {
		return OPT_TMP_FOLDER_SHORT
				+ " foldername | "
				+ OPT_TMP_FOLDER
				+ "=foldername\n\t\tspecifies a folder that the current user can write to to created multiple copies of the repository"
				+ "\n\t\tnote that by default the git repo specified will be used to check out all versions, even if a tmp folder is specified "
				+ "\n\t\tas long as the number of copies is not greater than 1.";
	}
}
