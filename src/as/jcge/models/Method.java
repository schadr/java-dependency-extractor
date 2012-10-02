package as.jcge.models;

import java.util.ArrayList;
import java.util.List;

public class Method
{
	private String file;
	private String pkg;
	private String clazz;
	private String name;
	private List<String> parameters;
	private int start = -1;
	private int end = -1;
	private boolean wasEdited = false;
	
	public Method() {
		parameters = new ArrayList<String>();
	}

	public Method(String file, String pkg, String clazz, String name, List<String> parameters, int start, int end, boolean wasEdited) {
		this.file = file;
		this.pkg = pkg;
		this.clazz = clazz;
		this.name = name;
		this.parameters = parameters;
		this.start = start;
		this.end = end;
		this.wasEdited = wasEdited;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public String getPkg() {
		return pkg;
	}

	public void setPkg(String pkg) {
		this.pkg = pkg;
	}

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void addParameter(String param) {
		parameters.add(param);
	}

	public List<String> getParameters() {
		return parameters;
	}

	public void setParameters(List<String> parameters) {
		this.parameters = parameters;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	@Override
	public String toString() {
		String val = pkg + "." + clazz + "." + name + 
				"(" + parameters.toString() + ")";
		return val;
	}

	public boolean wasModified() {
		return wasEdited;
	}

	public void setWasChanged(boolean b) {
		wasEdited = b;
	}
}
