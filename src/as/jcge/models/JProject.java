package as.jcge.models;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.core.dom.CompilationUnit;

import as.jcge.util.PathManager;

public class JProject {

	public Set<String> classPath;
	public PathManager sourcePath;
	public List<File> unParsedJavaFiles;
	public Map<String,CompilationUnit> cUnits;
}
