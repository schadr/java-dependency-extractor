package as.jde.test.util;

import java.io.File;

import as.jde.util.ProcessSpawner;

public class RepositoryManager {
	public static final String SELF_REPO_URI = "git://github.com/schadr/java-dependency-extractor.git";
	public static final String SELF_REPO_PATH = "./test-repos/test-self";
	
	private static RepositoryManager instance = null;
	
	public static RepositoryManager getInstance() {
		if (instance == null) instance = new RepositoryManager();
		return instance;
	}

	public String createSelfRepository() {
		createRepository(SELF_REPO_URI, SELF_REPO_PATH);
		
		return SELF_REPO_PATH;
	}
	
	public void createRepository(String repoUri, String path) {
		File p = new File(path);
		if (!p.exists()) {
			ProcessSpawner ps = new ProcessSpawner();
			ps.spawnProcess(new String[] {"git","clone",repoUri, path});
		}
	}

}
