package as.jcge.main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import as.jcge.models.CallGraph;
import as.jcge.output.XMLOutput;
import as.jcge.scm.SCMIterator;
import as.jcge.scm.git.GitController;


public class Main {
	public static void main(String[] args) throws ParserConfigurationException, TransformerException, IOException {
		Map<String,String> optArgs = ArgumentParser.parseArguments(args);
		GitController gc = new GitController(optArgs.get(ArgumentParser.OPT_REPOSITORY_LOCATION));
		SCMIterator iter = new SCMIterator(gc);
		XMLOutput outputter = new XMLOutput();
		
		File output = new File("/Users/schadr/Desktop/bulk.xml");
		output.createNewFile();
		BufferedWriter bw = new BufferedWriter(new FileWriter(output));

		Writer stdout = new OutputStreamWriter(System.out);
		stdout = bw;
		
		outputter.startOutput(stdout, gc.getRepositoryPath().split(File.separator)[gc.getRepositoryPath().split(File.separator).length-2]);
		while (iter.hasNext()) {
			try {
				CallGraph cg = iter.next();
				outputter.output(cg, stdout);
				stdout.write("\n");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		outputter.stopOutput(stdout);
		stdout.close();
	}
}
