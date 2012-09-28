package as.jcge.main;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import as.jcge.ast.CallGraphGenerator;
import as.jcge.db.DatabaseConnector;
import as.jcge.diff.UnifiedDiffParser;
import as.jcge.models.CallGraph;
import as.jcge.models.FileChange;
import as.jcge.models.Method;
import as.jcge.models.Pair;
import as.jcge.models.Range;
import as.jcge.scm.git.GitController;

public class NetworkBuilder {
	private DatabaseConnector 	db;
	private GitController 		gc;
	private UnifiedDiffParser 	udp;
	private CallGraphGenerator 	cgg;
	
	private CallGraph			cg;
	
	private String				HEAD;
	
	public NetworkBuilder(DatabaseConnector db) {
		this.db = db;
		gc = new GitController(Resources.repository);
		udp = new UnifiedDiffParser();
		cgg = new CallGraphGenerator(gc);
		cg = new CallGraph();
		
		HEAD = gc.getHead();
	}
	
	
	public void buildNetworks(List<String> commits) {
		for(String commit: commits)
			buildNetwork(commit);
		gc.reset(HEAD);
	}
	
	public void buildNetworks() throws IOException {
		List<String> commits = gc.getAllCommits();
		buildNetworks(commits);
	}
	
	public void buildNetworks(String commitFile) {
		List<String> commits = new ArrayList<String>();
		try {
			FileInputStream fstream = new FileInputStream(commitFile);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String line = null;
			while((line = br.readLine()) != null && !line.isEmpty()) {
				// TODO need proper check for comforming to commit id
				if (line.length() > 2)
					commits.add(line);
			}
			br.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		buildNetworks(commits);
	}
	
	private void buildNetwork(String commit) {
		// Clean the call graph
		cg = null;
		cg = new CallGraph();
		
		// Create the new call graph
		System.out.println("Creating the call graph");
		cgg.createCallGraphAtCommit(commit, cg);
		
		// Get the commit diff
		System.out.println("Diffing...");
		List<FileChange> changeset = udp.parse(gc.getCommitDiffJavaOnly(commit, gc.getAllFiles()));
		
		// Get author of commit
		String author = gc.getAuthorOfCommit(commit);
		
		// Get list of changed methods
		List<Pair<Method, Float>> changedMethods = getMethodsOfChangeset(changeset);
		
		// TODO output call graph
		// TODO output commit_info
		// TODO output commit_changes
		
	}
		
	private List<Pair<Method, Float>> getMethodsOfChangeset(List<FileChange> changesets) {
		List<Pair<Method, Float>> changedMethods = new ArrayList<Pair<Method, Float>>();
		for(FileChange changeset: changesets) {
			for(Range range: changeset.getRanges()) {
				updateChangedMethods(changedMethods, cg.getChangedMethods(changeset.getNewFile(), 
						range.getStart(), range.getEnd()));
			}
		}
		
		return changedMethods;
	}
	
	/**
	 * This method updates the list of changed methods so that
	 * you have no duplicate listings of methods (update their
	 * weight instead).
	 * @param methods
	 * @param method
	 */
	private void updateChangedMethods(List<Pair<Method, Float>> changedMethods, List<Pair<Method, Float>> list) {
		for(Pair<Method, Float> change: list) {
			boolean inserted = false;
			for(Pair<Method, Float> method: changedMethods) {
				if(method.getFirst().getStart() == change.getFirst().getStart() && 
						method.getFirst().getEnd() == change.getFirst().getEnd()) {
					method.setSecond(Math.min(method.getSecond() + change.getSecond(), 1.0f));
					inserted = true;
					break;
				}
			}
			if(!inserted)
				changedMethods.add(change);
		}
	}
	
	private List<Method> getCallers(Pair<Method, Float> method) {
			return cg.getCallersOfMethod((Method)method.getFirst());
	}
}
