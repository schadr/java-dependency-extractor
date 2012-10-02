package as.jcge.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PathManager {
	private Map<String, Integer> fPathSegments = new HashMap<String,Integer>();
	
	public void add(String path, String ignoreLeadingPath) {
		if (!path.endsWith(File.separator)) {
			path += File.separator + "dummy";
		} else {
			path += "dummy";
		}
		if (!ignoreLeadingPath.endsWith(File.separator)) ignoreLeadingPath += File.separator;
		
		Collection<String> pathSegments = getSubPaths(path, ignoreLeadingPath);
		
		addPathSegments(pathSegments);
	}
	
	public void addFile(String file, String ignoreLeadingPath) {
		if (!ignoreLeadingPath.endsWith(File.separator)) ignoreLeadingPath += File.separator;

		Collection<String> pathSegments = getSubPaths(file, ignoreLeadingPath);
		
		addPathSegments(pathSegments);
	}
	
	public Collection<String> getPaths() {
		return fPathSegments.keySet();
	}

	public void removeFile(String file, String ignoreLeadingPath) {
		if (!ignoreLeadingPath.endsWith(File.separator)) ignoreLeadingPath += File.separator;
		
		Collection<String> pathSegments = getSubPaths(file, ignoreLeadingPath);
		
		removePathSegments(pathSegments);
	}
	
	public void remove(String path, String ignoreLeadingPath) {
		if (!path.endsWith(File.separator)) {
			path += File.separator + "dummy";
		} else {
			path += "dummy";
		}
		if (!ignoreLeadingPath.endsWith(File.separator)) ignoreLeadingPath += File.separator;
		
		Collection<String> pathSegments = getSubPaths(path, ignoreLeadingPath);
		
		removePathSegments(pathSegments);
	}
	
	private Collection<String> getSubPaths(String fullyQualifiedFileName, String ignoreLeadingPath) {
		File f = new File(fullyQualifiedFileName);
		String path = f.getAbsolutePath();
		path = path.replace(ignoreLeadingPath, "");
		
		ArrayList<String> subpaths = new ArrayList<String>();
		
		int ci = path.indexOf(File.separator);
		while (ci != -1) {
			subpaths.add(ignoreLeadingPath + path.substring(0,ci));
			ci = path.indexOf(File.separator, ci+1);
		}
		
		return subpaths;
	}
	
	private void addPathSegments(Collection<String> pathSegments) {
		for (String pathSegment : pathSegments) {
			if (!fPathSegments.containsKey(pathSegment)) {
				fPathSegments.put(pathSegment, 1);
			} else {
				int count = fPathSegments.get(pathSegment);
				fPathSegments.put(pathSegment, count+1);
			}
		}
	}
	
	private void removePathSegments(Collection<String> pathSegments) {
		for (String pathSegment : pathSegments) {
			if (fPathSegments.containsKey(pathSegment)) {
				int count = fPathSegments.get(pathSegment);
				if (count == 1) fPathSegments.remove(pathSegment);
				else fPathSegments.put(pathSegment, count-1);
			}
		}
	}
}
