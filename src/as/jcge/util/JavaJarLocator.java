package as.jcge.util;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

 
public class JavaJarLocator {
	
	public List<File> javaFiles = new ArrayList<File>();
	public List<File> jarFiles = new ArrayList<File>();
	public Set<String> javaFilePaths = new HashSet<String>();
	public Set<String> jarFilePaths = new HashSet<String>();
  
	public void locate(File rootFolder) {
		traverse(rootFolder, new JavaDirectoryFilter(), rootFolder);
	}
	
	public void locate(File rootFolder, String[] ignoreFolderNames) {
		traverse(rootFolder, new JavaDirectoryFilter(ignoreFolderNames), rootFolder);
	}
	
	private void traverse(File path, FileFilter javaDirectoryFileFilter, File rootPath) {
		File allFilesAndDirectories[] = path.listFiles(javaDirectoryFileFilter);
		
		for (File entry : allFilesAndDirectories) {
			javaFilePaths.add(entry.getAbsolutePath());
			if (entry.isDirectory()) {
				traverse(entry, javaDirectoryFileFilter, rootPath);
			} else {
				if (entry.getName().toLowerCase().endsWith(".java")) {
					javaFiles.add(entry);
				} else {
					jarFiles.add(entry);
					jarFilePaths.add(entry.getAbsolutePath());
				}
			}
		}
	}
			
	private static class JavaDirectoryFilter implements FileFilter {
		private String[] extension = {"java", "jar"};
		private String[] ignoreFolders = {};

		public JavaDirectoryFilter() {
		}
		
		public JavaDirectoryFilter(String[] ignoreFolderNames) {
			ignoreFolders = ignoreFolderNames;
		}

		@Override
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
}
