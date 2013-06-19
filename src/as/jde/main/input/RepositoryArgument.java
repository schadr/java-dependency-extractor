package as.jde.main.input;

public class RepositoryArgument extends AbstractArgument {
	
	public static final String OPT_REPOSITORY = "--repository";
	public static final String OPT_REPOSITORY_SHORT = "-r";
	public static final String REPOSITORY_GIT = "git";
	public static final String DEFAULT = REPOSITORY_GIT;

	
	public RepositoryArgument() {
		super(OPT_REPOSITORY, DEFAULT);
	}
	
	@Override
	public void parseArgument(String argument) {
		super.parseArgument(argument, OPT_REPOSITORY);
	}

	@Override
	public void parseArgument(String flag, String value) {
		super.parseArgument(flag, value, OPT_REPOSITORY_SHORT);
	}

	@Override
	public String getHelpMessage() {
		return RepositoryArgument.OPT_REPOSITORY_SHORT 
			+ " type | "
			+ OPT_REPOSITORY
			+ "=type\n\t\tsepcifies the type of repository the source is contained, currently supported repos are "
			+ REPOSITORY_GIT
			+ " (default)";
	}	
}