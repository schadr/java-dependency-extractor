package as.jcge.scm;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.CompilationUnit;

import as.jcge.ast.JavaFileParser;
import as.jcge.ast.Visitor;
import as.jcge.models.CallGraph;
import as.jcge.models.FileChange;
import as.jcge.models.JProject;
import as.jcge.models.Range;
import as.jcge.scm.git.GitController;
import as.jcge.scm.git.UnifiedDiffParser;
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
		
		System.err.println(commitID);
		
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
		
		// parse files
		JavaFileParser parser = new JavaFileParser(fProject.classPath, fProject.sourcePath.getPaths(), fGit.getRepositoryPath());

		List<String> files = new ArrayList<String>();
		for (File file : fProject.javaFiles) {
			files.add(file.getAbsolutePath());
		}
		
		Map<String, CompilationUnit> cUnits = parser.parseFiles(files);
		
		// visit all compilation units
		for (String fullyQuallifiedFilename : cUnits.keySet()) {
			CompilationUnit unit = cUnits.get(fullyQuallifiedFilename);
			Visitor visitor = new Visitor(fullyQuallifiedFilename, unit, cg);
			unit.accept(visitor);
		}
		
		return cg;
	}

	private void markChangedMethods(String commitID, CallGraph cg) {
		UnifiedDiffParser diffParser = new UnifiedDiffParser(); 
		List<FileChange> changes = diffParser.parse(fGit.getCommitDiffHunkHeaders(commitID));
		for (FileChange change : changes) {
			String filename = change.getNewFile();
			if (filename != null && filename.endsWith("java")) {
				for (Range changeRange : change.getRanges()) {
					cg.setMethodsAsChanged(fGit.getRepositoryPath() + filename, changeRange.getStart(), changeRange.getEnd());
				}
			}
		}
	}

	private JProject initJavaProject() {
		JProject project = new JProject();
		
		String[] foldersToIgnrore = {".git"};
		
		JavaJarLocator locator = new JavaJarLocator();
		locator.locate(new File(fGit.getRepositoryPath()), foldersToIgnrore);
		project.classPath = locator.getJarFiles();
		project.sourcePath = locator.getJavaFilePaths();
		project.javaFiles = locator.getJavaFiles();
		
		return project;
	}

	private void updateJavaProject(JProject project, Map<String, List<String>> affectedFiles) {
		project.javaFiles.clear();
		
		for (String fullyQualifiedFileName : affectedFiles.get(GitController.ADD)) {
			if (fullyQualifiedFileName.toLowerCase().endsWith("java")) {
				project.javaFiles.add(new File(fullyQualifiedFileName));
				project.sourcePath.addFile(fullyQualifiedFileName, fGit.getRepositoryPath());
			} else if (fullyQualifiedFileName.toLowerCase().endsWith("jar")) {
				File jar = new File(fullyQualifiedFileName);
				project.classPath.add(jar.getAbsolutePath());
			}
		}
		
		for (String fullyQualifiedFileName : affectedFiles.get(GitController.MODIFY)) {
			if (fullyQualifiedFileName.toLowerCase().endsWith("java")) {
				project.javaFiles.add(new File(fullyQualifiedFileName));
			}
		}
		
		for (String fullyQualifiedFileName : affectedFiles.get(GitController.DELETE)) {
			project.javaFiles.remove(fullyQualifiedFileName);
			if (fullyQualifiedFileName.toLowerCase().endsWith("java")) {
				project.sourcePath.removeFile(fullyQualifiedFileName, fGit.getRepositoryPath());
			}
		}

		project.classPath.removeAll(affectedFiles.get(GitController.DELETE));
	}
}
