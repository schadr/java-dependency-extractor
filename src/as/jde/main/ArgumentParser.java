package as.jde.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import as.jde.main.input.Argument;
import as.jde.main.input.HelpArgument;
import as.jde.main.input.IgnoreFolderArgument;
import as.jde.main.input.OutputArgument;
import as.jde.main.input.QueueLimitArgument;
import as.jde.main.input.RepositoryArgument;
import as.jde.main.input.TempCopiesArgument;
import as.jde.main.input.TempFolderArgument;


public class ArgumentParser {

	public static final String OPT_REPOSITORY_LOCATION = "repository location";
	
	private List<Argument> arguments = new ArrayList<Argument>();
	
	public ArgumentParser() {
		arguments.add(new RepositoryArgument());
		arguments.add(new OutputArgument());
		arguments.add(new IgnoreFolderArgument());
		arguments.add(new QueueLimitArgument());
		arguments.add(new TempFolderArgument());
		arguments.add(new TempCopiesArgument());
		arguments.add(new HelpArgument());
	}
	
	public void printHelp() {
		System.out.println("./extract.sh [options] pathToRepository");
		System.out.println("options:");
		
		for (Argument arg : arguments) {
			System.out.print("\t");
			System.out.println(arg.getHelpMessage());
		}
	}
	
	public Map<String, String> parseArguments(String[] args) {
		Map<String,String> optArgs = new HashMap<String, String>();
		
		for (int i = 0; i < args.length - 1; i++) {
			if (args[i].startsWith("-")) {
				if (args[i+1].startsWith("-")) {
					for (Argument arg : arguments) {
						arg.parseArgument(args[i]);
					}
				} else if (i < args.length - 1){
					for (Argument arg : arguments) {
						arg.parseArgument(args[i], args[i+1]);
					}
					++i;
				}
			}
		}
		
		for (Argument arg : arguments) {
			optArgs.put(arg.getArgument().getKey(), arg.getArgument().getValue());
		}
		
		String repositoryLocation = args[args.length - 1];
		optArgs.put(OPT_REPOSITORY_LOCATION, repositoryLocation);
		
		return optArgs;
	}
}
