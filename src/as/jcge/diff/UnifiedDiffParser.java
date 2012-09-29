package as.jcge.diff;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import as.jcge.models.FileChange;
import as.jcge.models.Range;



public class UnifiedDiffParser {
	public static final String DIFF_OLD_FILE = "^---.*";
	public static final String DIFF_NEW_FILE = "^\\+\\+\\+.*";
	public static final String DIFF_HUNK_RANGE = "@@.*@@.*";
	public static final String DIFF_OLD_RANGE = "-[0-9]+,?[0-9]*";
	public static final String DIFF_NEW_RANGE = "\\+[0-9]+,?[0-9]*";
	
	private String fUnifiedDiff;
	
	private FileChange fCurrentChangeset;
	
	
	public UnifiedDiffParser(String unifiedDiff) {
		this.fUnifiedDiff = unifiedDiff;
	}
	
	public List<FileChange> parse() {
		if(fUnifiedDiff == null)
			return null;
		return parseUnifiedDiff();
	}
	
	public List<FileChange> parse(String unifiedDiff) {
		this.fUnifiedDiff = unifiedDiff;
		return parseUnifiedDiff();
	}
	
	private List<FileChange> parseUnifiedDiff() {
		List<FileChange> changes = new ArrayList<FileChange>();
		this.fCurrentChangeset = null;
		
		String[] lines = fUnifiedDiff.split(System.getProperty("line.separator"));
		for(int i = 0; i < lines.length; i++) {
			parseDiffLine(lines[i], changes);
		}
		if(fCurrentChangeset != null)
			changes.add(fCurrentChangeset);
		
		return changes;
	}
	
	private void parseDiffLine(String line, List<FileChange> changes) {
		if(line.matches(DIFF_OLD_FILE)) {
			if(fCurrentChangeset != null)
				changes.add(fCurrentChangeset);
			
			fCurrentChangeset = new FileChange();
			String oldFile = parseDiffOldFile(line);
			
			if(oldFile.startsWith("a/"))
				oldFile = oldFile.replaceFirst("a/", "");
			
			fCurrentChangeset.setOldFile(oldFile);
		}
		else if(line.matches(DIFF_NEW_FILE)) {
			if(fCurrentChangeset == null)
				fCurrentChangeset = new FileChange();
			
			String newFile = parseDiffNewFile(line);
			if(newFile.startsWith("b/"))
				newFile = newFile.replaceFirst("b/", "");
			
			fCurrentChangeset.setNewFile(newFile);
		}
		else if(line.matches(DIFF_HUNK_RANGE)) {
			Range range = parseDiffHunkRange(line);
			if(range != null)
				fCurrentChangeset.addRange(range);
		}
	}
	
	private String parseDiffOldFile(String line) {
		String[] split = line.split(" ");
		if(split.length == 2)
			return split[1];
		return "";
	}
	
	private String parseDiffNewFile(String line) {
		String[] split = line.split(" ");
		if(split.length == 2)
			return split[1];
		return "";
	}
	
	private Range parseDiffHunkRange(String line) {
		Range range = new Range();
		
		Pattern pattern = Pattern.compile(DIFF_NEW_RANGE);
		Matcher matcher = pattern.matcher(line);
		
		if(matcher.find()) {
			String[] ranges = matcher.group().split(",");
			// Strip the + symbol
			ranges[0] = ranges[0].substring(1);
			range.setStart(Integer.parseInt(ranges[0]));
			if(ranges.length > 1)
				range.setEnd(Integer.parseInt(ranges[0]) + Integer.parseInt(ranges[1]));
			else
				range.setEnd(Integer.parseInt(ranges[0]));
			
			return range;
		}
		else
			return null;
	}
}
