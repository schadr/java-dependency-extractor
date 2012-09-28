package as.jcge.scm.git;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import as.jcge.main.Resources;
import as.jcge.models.Owner;
import as.jcge.util.ProcessSpawner;

public class GitController {
	public final static String ADD = "A";
	public final static String DELETE = "D";
	public final static String MODIFY = "M";
	
	private ProcessSpawner fSpawner;
	private String fRepository;
	
	public GitController(String repositoryPath) {
		fRepository = repositoryPath;
		fSpawner = new ProcessSpawner(repositoryPath);
	}
	
	public String getRepositoryPath() {
		return fRepository;
	}
	
	public void reset(String commitID) {
		fSpawner.spawnProcess(new String[] {"git", "reset", "--hard", commitID});
	}
	
	public List<String> getAllCommits() {
		List<String> commits = new ArrayList<String>();
		parseLogForCommits(commits);
		return commits;
	}
	
	private void parseLogForCommits(List<String> commits) {
		String output = fSpawner.spawnProcess(new String[] {"git", "log", "--reverse", "--no-merges"});
		
		String[] lines = output.split(System.getProperty("line.separator"));
		for(int i = 0; i < lines.length; i++) {
			if(lines[i].matches(Resources.gitLogCommit)) {
				String[] split = lines[i].split(" ");
				commits.add(split[1]);
			}
		}
	}
	
	/**
	 * computes which files have been added/deleted/modified
	 * 
	 * note, the filename are all relative and not absolute
	 * 
	 * @param beforeCommitId
	 * @param afterCommitId
	 * @return
	 */
	public Map<String, List<String>> getAffectedFiles(String beforeCommitId, String afterCommitId) {
		String output = fSpawner.spawnProcess(new String[] {"git", "diff", "--name-status", beforeCommitId, afterCommitId});
		
		HashMap<String, List<String>> affectedFiles = new HashMap<String, List<String>>();
		affectedFiles.put(ADD, new ArrayList<String>());
		affectedFiles.put(DELETE, new ArrayList<String>());
		affectedFiles.put(MODIFY, new ArrayList<String>());
		
		String[] lines = output.split(System.getProperty("line.separator"));
		String type = null;
		for (String line : lines) {
			if (line.startsWith(ADD)) type = ADD;
			if (line.startsWith(DELETE)) type = DELETE;
			if (line.startsWith(MODIFY)) type = MODIFY;

			String filename = line.replaceFirst(type, "");
			filename = filename.replaceAll("\\s", "");
			affectedFiles.get(type).add(fRepository + File.pathSeparator + filename);
		}
		
		return affectedFiles;
	}
	
	public String getCommitDiff(String commit) {
		return fSpawner.spawnProcess(new String[] {"git", "diff-tree", "--unified=0", commit});
	}
	
	public String getCommitDiffJavaOnly(String commit, List<String> javaFiles) {
		if(javaFiles.isEmpty())
			return getCommitDiff(commit);
		else {
			javaFiles.add(0, "--");
			javaFiles.add(0, commit);
			javaFiles.add(0, "--unified=0");
			javaFiles.add(0, "diff-tree");
			javaFiles.add(0, "git");
		}
		return fSpawner.spawnProcess(javaFiles);
	}
	
	public String getAuthorOfCommit(String commit) {
		return fSpawner.spawnProcess(new String[] {"git", "show", "-s", "--format=%ce", commit})
				.replace("\n", "");
	}
	
	public List<Owner> getOwnersOfFileRange(String file, int start, int end) {
		List<Owner> owners = new ArrayList<Owner>();
		
		String output = fSpawner.spawnProcess(new String[] {"git", "blame", "-e", "-L"+start+","+end, file});
		String[] lines = output.split(System.getProperty("line.separator"));
		
		for(int i = 0; i < lines.length; i++) {
			Pattern pattern = Pattern.compile(Resources.gitBlame);
			Matcher matcher = pattern.matcher(lines[i]);

			if(matcher.find()) {
				Owner owner = new Owner();
				owner.setEmail(matcher.group(1));
				owner.setOwnership((float)1/(end-start+1));
				updateOwnersList(owners, owner);
			}
		}
		
		return owners;
	}
	
	private void updateOwnersList(List<Owner> owners, Owner owner) {
		for(Owner own: owners) {
			if(own.getEmail().equals(owner.getEmail())) {
				own.setOwnership(own.getOwnership()+owner.getOwnership());
				return;
			}
		}
		owners.add(owner);
	}
	
	/**
	 * Returns the HEAD commit ID of the repository.
	 * @return
	 */
	public String getHead() {
		String output = fSpawner.spawnProcess(new String[] {"git", "rev-parse", "HEAD"});
		String[] lines = output.split(System.getProperty("line.separator"));
		
		Pattern pattern = Pattern.compile(Resources.gitHead);
		Matcher matcher = pattern.matcher(lines[0]);
		
		if(matcher.find()) {
			return lines[0];
		}
		
		return null;
	}
}
