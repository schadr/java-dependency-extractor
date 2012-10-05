package as.jcge.models;

import java.io.File;
import java.util.List;
import java.util.Set;

import as.jcge.util.PathManager;

public class JProject {

	public Set<String> classPath;
	public PathManager sourcePath;
	public List<File> javaFiles;
}
