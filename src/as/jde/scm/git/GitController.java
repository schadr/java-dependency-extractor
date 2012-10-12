package as.jde.scm.git;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import as.jde.models.Owner;
import as.jde.scm.Commit;
import as.jde.util.ProcessSpawner;

public class GitController {
	public final static String ADD = "A";
	public final static String DELETE = "D";
	public final static String MODIFY = "M";
	
	private static final String GIT_HEAD = "[a-z0-9]+";
	private static final String GIT_BLAME = "\\<(.+?)\\>";
	
	private ProcessSpawner fSpawner;
	private String fRepository;
	
	public GitController(String repositoryPath) {
		fRepository = repositoryPath;
		fSpawner = new ProcessSpawner(repositoryPath);
	}
	
	public String getRepositoryPath() {
		return fRepository;
	}
	
	/**
	 * resets the repository to a specific commit
	 * 
	 * @param commitID the commitID the repository is supposed to be set to
	 */
	public void reset(String commitID) {
		fSpawner.spawnProcess(new String[] {"git", "reset", "--hard", commitID});
	}
	
	/**
	 * gets a list of all commits in the repository
	 * 
	 * @return list of commitids
	 */
	public List<String> getAllCommits() {
		List<String> commits = new ArrayList<String>();
		String output = fSpawner.spawnProcess(new String[] {"git", "rev-list", "--all", "--no-merges", "--pretty=oneline", "--reverse"});
		
		String[] lines = output.split(System.getProperty("line.separator"));
		for (String line : lines) {
			commits.add(line.split(" ")[0]);
		}
		
		return commits;
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

		if (output.length() == 0)
			return affectedFiles;
		
		String[] lines = output.split(System.getProperty("line.separator"));
		String type = null;
		for (String line : lines) {
			if (line.startsWith(ADD)) type = ADD;
			if (line.startsWith(DELETE)) type = DELETE;
			if (line.startsWith(MODIFY)) type = MODIFY;

			String filename = line.replaceFirst(type, "");
			filename = filename.replaceAll("\\s", "");
			affectedFiles.get(type).add(fRepository + filename);
		}
		
		return affectedFiles;
	}
	
	/**
	 * TODO should be returning a list of fileChanges
	 * 
	 * @param commitID
	 * @return String containing the command line diff output
	 */
	public String getCommitDiff(String commitID) {
		return fSpawner.spawnProcess(new String[] {"git", "diff-tree", "--unified=0", commitID});
	}
	
	/**
	 * TODO should be returning a list of fileChanges
	 * 
	 * @param commitID
	 * @param files files that should be diffed
	 * @return one string blobb containing all the diff information
	 */
	public String getCommitDiff(String commitID, List<String> files) {
		if(files.isEmpty())
			return getCommitDiff(commitID);
		else {
			files.add(0, "--");
			files.add(0, commitID);
			files.add(0, "--unified=0");
			files.add(0, "diff-tree");
			files.add(0, "git");
		}
		return fSpawner.spawnProcess(files);
	}
	
	public String getCommitDiffHunkHeaders(String commitID) {
		return fSpawner.spawnProcess(new String[] {"git", "diff-tree", "--unified=0", commitID, "|" , "grep" , "'+++|---|@@'"});
	}
	
	public String getCommitDiffHunkHeaders(String commitID, List<String> files) {
		if(files.isEmpty())
			return getCommitDiff(commitID);
		else {
			files.add(0, "--");
			files.add(0, commitID);
			files.add(0, "--unified=0");
			files.add(0, "diff-tree");
			files.add(0, "git");
			files.add("|");
			files.add("grep");
			files.add("'+++|---|@@'");
		}
		return fSpawner.spawnProcess(files);
	}
	
	/**
	 * returns the author of the commit
	 * 
	 * @param commitID
	 * @return auther string
	 */
	public String getAuthorOfCommit(String commitID) {
		return fSpawner.spawnProcess(new String[] {"git", "show", "-s", "--format=%ce", commitID}).replace("\n", "");
	}
	
	/**
	 * returns the date of the commit
	 * @param commitID
	 * @return date in string form
	 */
	public String getDateOfCommit(String commitID) {
		return fSpawner.spawnProcess(new String[] {"git", "show", "-s", "--format=%cd", commitID}).replace("\n", "");
	}
	
	/**
	 * returns the authors that last edited the file in the given line range
	 * 
	 * @param file
	 * @param startLine
	 * @param endLine
	 * @return
	 */
	public List<Owner> getOwnersOfFileRange(String file, int startLine, int endLine) {
		List<Owner> owners = new ArrayList<Owner>();
		
		String output = fSpawner.spawnProcess(new String[] {"git", "blame", "-e", "-L"+startLine+","+endLine, file});
		String[] lines = output.split(System.getProperty("line.separator"));
		
		Pattern pattern = Pattern.compile(GIT_BLAME);
		for(int i = 0; i < lines.length; i++) {
			Matcher matcher = pattern.matcher(lines[i]);

			if(matcher.find()) {
				Owner owner = new Owner();
				owner.setEmail(matcher.group(1));
				owner.setOwnership((float)1/(endLine-startLine+1));
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
		
		Pattern pattern = Pattern.compile(GIT_HEAD);
		Matcher matcher = pattern.matcher(lines[0]);
		
		if(matcher.find()) {
			return lines[0];
		}
		
		return null;
	}

	/**
	 * return a commit information object
	 * 
	 * @param commitID
	 * @return
	 */
	public Commit getCommitInfo(String commitID) {
		Commit c = new Commit();
		
		c.author = getAuthorOfCommit(commitID);
		c.commitID = commitID;
		c.time = getDateOfCommit(commitID);
		
		return c;
	}
}
