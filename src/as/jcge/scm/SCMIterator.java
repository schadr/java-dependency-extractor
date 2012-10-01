package as.jcge.scm;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.CompilationUnit;

import as.jcge.ast.JavaFileParser;
import as.jcge.ast.Visitor;
import as.jcge.diff.UnifiedDiffParser;
import as.jcge.models.CallGraph;
import as.jcge.models.FileChange;
import as.jcge.models.JProject;
import as.jcge.models.Range;
import as.jcge.scm.git.GitController;
import as.jcge.util.JavaJarLocator;

public class SCMIterator {
	private GitController fGit = null;
	private int fCurrentCommit = -1;
	private List<String> fCommits = null;
	
	private JProject fProject = null;
	
	public SCMIterator(GitController gitController) {
		fGit = gitController;
		fCommits = fGit.getAllCommits();
	}

	public boolean hasNext() {
		return fCommits.size()-1 > fCurrentCommit ;
	}

	synchronized public CallGraph next() {
		fCurrentCommit++;
		String commitID = fCommits.get(fCurrentCommit);
		
		// checkout next revision
		fGit.reset(commitID);
		
		if (fCurrentCommit == 0) {
			fProject = initJavaProject();
		} else {
			Map<String, List<String>> affectedFiles = fGit.getAffectedFiles(fCommits.get(fCurrentCommit-1), commitID);
			updateJavaProject(fProject, affectedFiles);
		}
	
		CallGraph cg = createCallGraph(commitID);
		
		markChangedMethods(commitID, cg);
		
		return cg;
	}

	private CallGraph createCallGraph(String commitID) {
		CallGraph cg = new CallGraph(fGit.getCommitInfo(commitID));
		
		// parse new/modified files
		JavaFileParser parser = new JavaFileParser(fProject.classPath, fProject.sourcePath, fGit.getRepositoryPath());
		for(File file: fProject.unParsedJavaFiles) {
			CompilationUnit unit = parser.parseFile(file.getAbsolutePath(), cg);
			String fullyQuallifiedFilename = file.getAbsolutePath() + File.separator + file.getName();
			fProject.cUnits.put(fullyQuallifiedFilename, unit);
		}

		// visit all compilation units
		for (String fullyQuallifiedFilename : fProject.cUnits.keySet()) {
			CompilationUnit unit = fProject.cUnits.get(fullyQuallifiedFilename);
			Visitor visitor = new Visitor(fullyQuallifiedFilename, unit, cg);
			unit.accept(visitor);
		}
		return cg;
	}

	private void markChangedMethods(String commitID, CallGraph cg) {
		UnifiedDiffParser diffParser = new UnifiedDiffParser( fGit.getCommitDiff(commitID)); 
		List<FileChange> changes = diffParser.parse();
		for (FileChange change : changes) {
			String filename = change.getNewFile();
			for (Range changeRange : change.getRanges()) {
				cg.setMethodsAsChanged(filename, changeRange.getStart(), changeRange.getEnd());
			}
		}
	}

	private JProject initJavaProject() {
		JProject project = new JProject();
		
		String[] foldersToIgnrore = {".git"};
		
		JavaJarLocator locator = new JavaJarLocator();
		locator.locate(new File(fGit.getRepositoryPath()), foldersToIgnrore);
		project.classPath = locator.jarFilePaths;
		project.sourcePath = locator.javaFilePaths;
		project.unParsedJavaFiles = locator.javaFiles;
		project.cUnits = new HashMap<String,CompilationUnit>();
		
		return project;
	}

	private void updateJavaProject(JProject project, Map<String, List<String>> affectedFiles) {
		project.unParsedJavaFiles.clear();
		
		for (String fullyQualifiedFileName : affectedFiles.get(GitController.ADD)) {
			if (fullyQualifiedFileName.toLowerCase().endsWith("java")) {
				project.unParsedJavaFiles.add(new File(fullyQualifiedFileName));
				project.sourcePath.addAll(getSubPaths(fullyQualifiedFileName,fGit.getRepositoryPath()));
			} else {
				File jar = new File(fullyQualifiedFileName);
				project.classPath.add(jar.getAbsolutePath());
			}
		}
		
		for (String fullyQualifiedFileName : affectedFiles.get(GitController.MODIFY)) {
			if (fullyQualifiedFileName.toLowerCase().endsWith("java")) {
				project.unParsedJavaFiles.add(new File(fullyQualifiedFileName));
				project.cUnits.remove(fullyQualifiedFileName);
			}
		}
		
		for (String fullyQualifiedFileName : affectedFiles.get(GitController.DELETE)) {
			project.cUnits.remove(fullyQualifiedFileName);
		}
	}

	private Collection<String> getSubPaths(String fullyQualifiedFileName, String repositoryPath) {
		File f = new File(fullyQualifiedFileName);
		String path = f.getAbsolutePath();
		path = path.replace(repositoryPath, "");
		
		ArrayList<String> subpaths = new ArrayList<String>();
		
		int ci = path.indexOf(File.pathSeparator);
		while (ci != -1) {
			subpaths.add(repositoryPath + File.separator + path.substring(0,ci));
			ci = path.indexOf(File.pathSeparator, ci);
		}
		
		return subpaths;
	}
}
