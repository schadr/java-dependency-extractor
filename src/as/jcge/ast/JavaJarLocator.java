package as.jcge.ast;

import java.io.File;
import java.io.FileFilter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

 
public class JavaJarLocator {
	
	public List<File> fJavaFiles;
	public List<File> fJarFiles;
	public Set<String> fJavaFilePaths = new HashSet<String>();
	public Set<String> fJarFilePaths = new HashSet<String>();
  
	public void locate(File rootFolder) {
		traverse(rootFolder, new JavaDirectoryFilter(), rootFolder);
	}
	
	private void traverse(File path, FileFilter javaDirectoryFileFilter, File rootPath) {
		File allFilesAndDirectories[] = path.listFiles(javaDirectoryFileFilter);
		
		for (File entry : allFilesAndDirectories) {
			fJavaFilePaths.add(entry.getAbsolutePath());
			if (entry.isDirectory()) {
				traverse(entry, javaDirectoryFileFilter, rootPath);
			} else {
				if (entry.getName().toLowerCase().endsWith(".java")) {
					fJavaFiles.add(entry);
				} else {
					fJarFiles.add(entry);
					fJarFilePaths.add(entry.getAbsolutePath());
				}
			}
		}
	}
			
	private static class JavaDirectoryFilter implements FileFilter {
		private String[] extension = {"java", "jar"};

		@Override
		public boolean accept(File pathname) {
			if (pathname.isDirectory()) {
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
