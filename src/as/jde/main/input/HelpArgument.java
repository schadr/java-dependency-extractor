package as.jde.main.input;

public class HelpArgument extends AbstractArgument {
	
	public final static String OPT_HELP = "--help";
	public final static String OPT_HELP_SHORT = "-h";
	public final static String HELP_FALSE = "false";
	
	public HelpArgument() {
		super(OPT_HELP, HELP_FALSE);
	}

	@Override
	public void parseArgument(String argument) {
		super.parseArgument(argument, OPT_HELP);
	}
	
	@Override
	public void parseArgument(String input_flag, String value) {
		super.parseArgument(input_flag, value, OPT_HELP_SHORT);
	}

	@Override
	public String getHelpMessage() {
		return OPT_HELP_SHORT
				+ " | "
				+ OPT_HELP
				+ "\n\t\tprints this help";
	}
}
