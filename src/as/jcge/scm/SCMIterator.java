package as.jcge.scm;

import java.io.File;
import java.util.List;

import as.jcge.scm.git.GitController;

public class SCMIterator {
	private GitController fGit = null;
	private boolean fInitilized = false;
	private int fCurrentCommit = 0;
	private List<String> fCommits = null;
	
	public SCMIterator(GitController gitController, File eclipseConfigFile) {
		fGit = gitController;
	}

	public boolean hasNext() {
		if (!fInitilized) {
			fCommits = fGit.getAllCommits();
			fInitilized = true;
		}
		return fCommits.size() > fCurrentCommit ;
	}

	public Commit next() {
		// checkout next revision
		
		// create config file
		
		
		// TODO Auto-generated method stub
		return null;
	}
}
