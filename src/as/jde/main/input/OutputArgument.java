package as.jde.main.input;

public class OutputArgument extends AbstractArgument {
	
	public static final String OPT_OUTPUT_FORMAT = "--output-format";
	public static final String OPT_OUTPUT_FORMAT_SHORT = "-f";
	public static final String OUTPUT_XML = "xml";
	public static final String DEFAULT = OUTPUT_XML;
	
	public OutputArgument() {
		super(OPT_OUTPUT_FORMAT, DEFAULT);
	}

	@Override
	public void parseArgument(String argument) {
		super.parseArgument(argument, OPT_OUTPUT_FORMAT);
	}

	@Override
	public void parseArgument(String flag, String value) {
		super.parseArgument(flag, value, OPT_OUTPUT_FORMAT_SHORT);
	}

	@Override
	public String getHelpMessage() {
		return OPT_OUTPUT_FORMAT_SHORT
			+ " type | "
			+ OPT_OUTPUT_FORMAT
			+ "=type\n\t\tspecifies the format of the output, currently supported are "
			+ OUTPUT_XML
			+ " (default)";
	}
}
