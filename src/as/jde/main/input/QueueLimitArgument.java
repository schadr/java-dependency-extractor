package as.jde.main.input;


public class QueueLimitArgument extends AbstractArgument {

	public static final String OPT_QUEUE_LIMIT = "--queue-size";
	public static final String OPT_QUEUE_LIMIT_SHORT = "-q";
	public static final String DEFAULT = "10";

	public QueueLimitArgument() {
		super(OPT_QUEUE_LIMIT, DEFAULT);
	}
	
	@Override
	public void parseArgument(String argument) {
		super.parseArgument(argument, OPT_QUEUE_LIMIT);
	}

	@Override
	public void parseArgument(String flag, String value) {
		super.parseArgument(flag, value, OPT_QUEUE_LIMIT_SHORT);
	}

	@Override
	public String getHelpMessage() {
		return OPT_QUEUE_LIMIT_SHORT
				+ " size | "
				+ OPT_QUEUE_LIMIT
				+ "=size\n\t\tspecifies the size of the output queue (default: "
				+ DEFAULT
				+ ") (-1: no limit)";
	}
}
