package as.jde.models;

import java.io.File;
import java.util.List;
import java.util.Set;

import as.jde.util.PathManager;

public class JProject {

	public Set<String> classPath;
	public PathManager sourcePath;
	public List<File> javaFiles;
}
