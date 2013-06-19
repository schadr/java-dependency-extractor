package as.jde.main;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import as.jde.graph.CallGraph;
import as.jde.main.input.HelpArgument;
import as.jde.main.input.IgnoreFolderArgument;
import as.jde.main.input.QueueLimitArgument;
import as.jde.output.ThreadedOutput;
import as.jde.output.XMLOutput;
import as.jde.scm.SCMIterator;
import as.jde.scm.git.GitController;


public class Main {
	public static void main(String[] args) throws ParserConfigurationException, TransformerException, IOException, InterruptedException {
		ArgumentParser argumentParser = new ArgumentParser();
		Map<String,String> optArgs = argumentParser.parseArguments(args);
		
		if (helpFlagPresent(optArgs)) {
			argumentParser.printHelp();
			return;
		}
		
		GitController gc = new GitController(optArgs.get(ArgumentParser.OPT_REPOSITORY_LOCATION));
		SCMIterator iter = new SCMIterator(gc, optArgs.get(IgnoreFolderArgument.OPT_IGNORE_FOLDER));
		Writer stdout = new OutputStreamWriter(System.out);
		XMLOutput outputter = new XMLOutput(stdout);
		ThreadedOutput out = new ThreadedOutput(outputter, Integer.parseInt(optArgs.get(QueueLimitArgument.OPT_QUEUE_LIMIT)));
		
		out.start(gc.getRepositoryPath().split(File.separator)[gc.getRepositoryPath().split(File.separator).length-2]);
		while (iter.hasNext()) {
			try {
				CallGraph cg = iter.next();
				out.add(cg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		out.stop();
		stdout.close();
	}

	private static boolean helpFlagPresent(Map<String, String> optArgs) {
		return !optArgs.get(HelpArgument.OPT_HELP).equals(HelpArgument.HELP_FALSE);
	}
}
