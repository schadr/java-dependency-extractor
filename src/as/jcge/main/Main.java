package as.jcge.main;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import as.jcge.models.CallGraph;
import as.jcge.output.ThreadedOutput;
import as.jcge.output.XMLOutput;
import as.jcge.scm.SCMIterator;
import as.jcge.scm.git.GitController;


public class Main {
	public static void main(String[] args) throws ParserConfigurationException, TransformerException, IOException, InterruptedException {
		Map<String,String> optArgs = ArgumentParser.parseArguments(args);
		GitController gc = new GitController(optArgs.get(ArgumentParser.OPT_REPOSITORY_LOCATION));
		SCMIterator iter = new SCMIterator(gc);
		Writer stdout = new OutputStreamWriter(System.out);
		XMLOutput outputter = new XMLOutput(stdout);
		ThreadedOutput out = new ThreadedOutput(outputter);
		
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
}
