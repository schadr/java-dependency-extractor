package as.jcge.main;

import java.util.Map;

import as.jcge.models.CallGraph;
import as.jcge.output.XMLOutputter;
import as.jcge.scm.SCMIterator;
import as.jcge.scm.git.GitController;


public class Main {
	public static void main(String[] args) {
		Map<String,String> optArgs = ArgumentParser.parseArguments(args);
		GitController gc = new GitController(optArgs.get(ArgumentParser.OPT_REPOSITORY_LOCATION));
		SCMIterator iter = new SCMIterator(gc);
		XMLOutputter outputter = new XMLOutputter();
		while (iter.hasNext()) {
			CallGraph cg = iter.next();
			outputter.output(cg);
		}
	}
}
