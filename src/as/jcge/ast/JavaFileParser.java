package as.jcge.ast;

import java.io.File;
import java.io.FileInputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import as.jcge.main.Resources;
import as.jcge.models.CallGraph;

public class JavaFileParser {
	private Collection<String> fClassPath;
	private Collection<String> fSourcePath;
	private List<String> fEncodings;
	
	public JavaFileParser(Collection<String> classPath, Collection<String> sourcePath) {
		fClassPath = classPath;
		fSourcePath = sourcePath;
		fEncodings = new ArrayList<String>();
	}
	
	/**
	 * Parsers a given file using the ASTParser.
	 * @param file
	 */
	public CompilationUnit parseFile(String file, CallGraph cg) {
		// Create parse for JRE 1.0 - 1.6
		ASTParser parser= ASTParser.newParser(AST.JLS3);

		// Set up parser
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setResolveBindings(true);
		parser.setBindingsRecovery(true);
		@SuppressWarnings("unchecked")
		Hashtable<String, String> options = JavaCore.getDefaultOptions();
		options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_6);
		parser.setCompilerOptions(options);
		parser.setEnvironment(fClassPath.toArray(new String[fClassPath.size()]), 
				fSourcePath.toArray(new String[fSourcePath.size()]), 
				fEncodings.toArray(new String[fEncodings.size()]), true);

		// Set up the file to parse
		String fileRaw = readFile(file);
		if(fileRaw == null)
			return null;
		parser.setSource(fileRaw.toCharArray());
		String unitName = generateUnitName(file);
		if(unitName == null)
			return null;
		parser.setUnitName(unitName);

		
		// Parse
		CompilationUnit unit = (CompilationUnit) parser.createAST(null);

		return unit;
		
		// Visit the syntax tree

	}
	
	/**
	 * This function reads a file into a String.
	 * @param path The absolute path.
	 * @return
	 */
	private String readFile(String path) {
		try {
			FileInputStream stream = new FileInputStream(new File(path));
			FileChannel fc = stream.getChannel();
			MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
			/* Instead of using default, pass in a decoder. */
			stream.close();
			return Charset.defaultCharset().decode(bb).toString();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Generates the unit name needed for the AST parser based on
	 * the repository and file absolute path.
	 * @param file The absolute path.
	 * @return
	 */
	private String generateUnitName(String file) {
		// Get repository name
		String repoName = Resources.repository.substring(Resources.repository.lastIndexOf("/")+1);
		
		// Get starting point of unit name
		int start = file.indexOf(Resources.repository) + Resources.repository.length() - repoName.length();
		
		// Get the unit name
		String unitName = file.substring(start);
		
		return unitName;
	}
}