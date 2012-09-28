package as.jcge.ast;

import java.io.File;
import java.io.IOException;

import as.jcge.main.Resources;
import as.jcge.models.CallGraph;
import as.jcge.scm.git.GitController;
import as.jcge.util.JavaJarLocator;

public class CallGraphGenerator {
	private GitController fGc;
	
	public CallGraphGenerator(GitController gc) {
		fGc = gc;
	}
	
	/**
	 * Creates a new call graph in the database from scratch.
	 * @param commitID
	 * @throws IOException 
	 */
	public void createCallGraphAtCommit(String commitID, CallGraph cg) {
		// Set the repo to the commit
		fGc.reset(commitID);
		
		// Set up the config file
		JavaJarLocator locator = new JavaJarLocator();
		locator.locate(new File(Resources.configFile));
		
		// Parse each file
		for(File file: locator.fJavaFiles) {
			JavaFileParser parser = new JavaFileParser(locator.fJarFilePaths, locator.fJavaFilePaths);
			parser.parseFile(file.getAbsolutePath(), cg);
		}
	}
}
