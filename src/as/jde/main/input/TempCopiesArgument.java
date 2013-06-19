package as.jde.main.input;


public class TempCopiesArgument extends AbstractArgument {

	public static final String OPT_TMP_COPIES = "--num-copies";
	public static final String OPT_TMP_COPIES_SHORT = "-n";
	public static final String DEFAULT = "1";
		
	public TempCopiesArgument() {
		super(OPT_TMP_COPIES, DEFAULT);
	}

	@Override
	public void parseArgument(String argument) {
		super.parseArgument(argument, OPT_TMP_COPIES);
	}

	@Override
	public void parseArgument(String flag, String value) {
		super.parseArgument(flag, value, OPT_TMP_COPIES_SHORT);
	}

	@Override
	public String getHelpMessage() {
		return OPT_TMP_COPIES_SHORT
				+ " number_of_copies | "
				+ OPT_TMP_COPIES
				+ "=number_of_copies\n\t\tspecifies the number of copies of repo made, if a folder for the copies was specified (default="
				+ DEFAULT
				+ ").";
	}
}
