package as.jcge.models;

import java.util.ArrayList;
import java.util.List;

public class FileChange {
	private String oldFile;
	private String newFile;
	private List<Range> ranges;
	
	public FileChange() {
		super();
		ranges = new ArrayList<Range>();
	}

	public FileChange(String oldFile, String newFile, List<Range> ranges) {
		super();
		this.oldFile = oldFile;
		this.newFile = newFile;
		this.ranges = ranges;
	}

	public String getOldFile() {
		return oldFile;
	}

	public void setOldFile(String oldFile) {
		this.oldFile = oldFile;
	}

	public String getNewFile() {
		return newFile;
	}

	public void setNewFile(String newFile) {
		this.newFile = newFile;
	}

	public List<Range> getRanges() {
		return ranges;
	}

	public void setRanges(List<Range> ranges) {
		this.ranges = ranges;
	}
	
	public void addRange(Range range) {
		this.ranges.add(range);
	}
}
