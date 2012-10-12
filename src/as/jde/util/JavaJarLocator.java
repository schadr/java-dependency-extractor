package as.jde.util;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
 
public class JavaJarLocator {
	
	public List<File> javaFiles = new ArrayList<File>();
	public Set<String> jarFiles = new HashSet<String>();
	public PathManager javaFilePaths = new PathManager();
  
	
	public void locate(File rootFolder, String[] ignoreFolderNames, Pattern ignoreFolderPattern) {
		traverse(rootFolder, new JavaDirectoryFilter(ignoreFolderNames), rootFolder, ignoreFolderPattern);
	}
	
	private void traverse(File path, FileFilter javaDirectoryFileFilter, File rootPath, Pattern ignoreFolderPattern) {
		File allFilesAndDirectories[] = path.listFiles(javaDirectoryFileFilter);
		
		for (File entry : allFilesAndDirectories) {
			if (entry.isDirectory()) {
				Matcher m = ignoreFolderPattern.matcher(entry.getAbsolutePath());
				if (!m.matches()) { 
					javaFilePaths.add(entry.getAbsolutePath(), rootPath.getAbsolutePath().toString());
					traverse(entry, javaDirectoryFileFilter, rootPath, ignoreFolderPattern);
				}
			} else {
				if (entry.getName().toLowerCase().endsWith(".java")) {
					javaFiles.add(entry);
				} else {
					jarFiles.add(entry.getAbsolutePath());
				}
			}
		}
	}
			
	private static class JavaDirectoryFilter implements FileFilter {
		private String[] extension = {"java", "jar"};
		private String[] ignoreFolders = {};
		
		public JavaDirectoryFilter(String[] ignoreFolderNames) {
			ignoreFolders = ignoreFolderNames;
		}

		public boolean accept(File pathname) {
			if (pathname.isDirectory()) {
				for (String f : ignoreFolders) {
					if (pathname.getAbsoluteFile().toString().toLowerCase().contains(f)) {
						return false;
					}
				}
				return true;
			}

			String name = pathname.getName().toLowerCase();
			for (String anExt : extension) {
				if (name.endsWith(anExt)) {
					return true;
				}
			}
			return false;
		}
	}

	public Set<String> getJarFiles() {
		return jarFiles;
	}

	public PathManager getJavaFilePaths() {
		return javaFilePaths;
	}

	public List<File> getJavaFiles() {
		return javaFiles;
	}
}
