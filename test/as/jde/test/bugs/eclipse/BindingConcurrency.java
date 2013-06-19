package as.jde.test.bugs.eclipse;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FileASTRequestor;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.IPackageBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.junit.Test;

public class BindingConcurrency {

	@Test
	public void shouldNotDeadlock() {
		List<String> javaFilePaths = new ArrayList<String>();
		List<String> javaFiles = new ArrayList<String>();
		List<String> jarFiles = new ArrayList<String>();

		// gather all jar files and java files
		traverse(new File("."), new JavaDirectoryFilter(), javaFilePaths, javaFiles, jarFiles);

		String[] encodings1 = new String[javaFilePaths.size()];
		for (int i = 0; i < encodings1.length; ++i) encodings1[i] = "UTF=8";


		while (true) {
			System.err.println("Runn started ... ");
			// parse files
			// Create parse for JRE 1.0 - 1.6
			ASTParser parser= ASTParser.newParser(AST.JLS4);

			// Set up parser
			parser.setKind(ASTParser.K_COMPILATION_UNIT);
			parser.setResolveBindings(true);
			parser.setBindingsRecovery(true);
			@SuppressWarnings("unchecked")
			Hashtable<String, String> options = JavaCore.getDefaultOptions();
			options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_6);
			parser.setCompilerOptions(options);
			parser.setEnvironment(jarFiles.toArray(new String[jarFiles.size()]), 
					javaFilePaths.toArray(new String[javaFilePaths.size()]), 
					encodings1, true);

			final Map<String, CompilationUnit> cUnits = new HashMap<String, CompilationUnit>();
			FileASTRequestor ar = new FileASTRequestor() {			
				@Override
				public void acceptAST(String source, CompilationUnit ast) {
					cUnits.put(source, ast);
				}
			};

			String[] encodings = new String[javaFiles.size()];
			for (int i = 0; i < encodings.length; ++i) encodings[i] = "UTF-8";
			parser.createASTs(javaFiles.toArray(new String[javaFiles.size()]), encodings, new String[0], ar, null);


			ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

			for (final String fullyQuallifiedFilename : cUnits.keySet()) {
				Runnable r = new Runnable() {
					public void run() {
						CompilationUnit unit = cUnits.get(fullyQuallifiedFilename);
						MyVisitor visitor = new MyVisitor();
						unit.accept(visitor);					
					}
				};
				executor.execute(r);
			}

			executor.shutdown();

			while (!executor.isTerminated()) {}
			System.err.println(" DONE");
		}
	}


	private void traverse(File path, FileFilter javaDirectoryFileFilter, List<String> javaFilePaths, List<String> javaFiles, List<String> jarFiles) {
		File allFilesAndDirectories[] = path.listFiles(javaDirectoryFileFilter);

		for (File entry : allFilesAndDirectories) {
			if (entry.isDirectory()) {
				javaFilePaths.add(entry.getAbsolutePath());
				traverse(entry, javaDirectoryFileFilter, javaFilePaths, javaFiles, jarFiles);
			} else {
				if (entry.getName().toLowerCase().endsWith(".java")) {
					javaFiles.add(entry.getAbsolutePath());
				} else {
					jarFiles.add(entry.getAbsolutePath());
				}
			}
		}
	}

	class JavaDirectoryFilter implements FileFilter {
		private String[] extension = {"java", "jar"};

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

	class MyVisitor extends ASTVisitor {
		@Override
		public boolean visit(MethodDeclaration node) {
			IMethodBinding binding = node.resolveBinding();
			if (binding != null) {
				binding.getName();
				ITypeBinding[] parameterTypes = binding.getParameterTypes();
				for (ITypeBinding t : parameterTypes) 
					t.getQualifiedName();
				ITypeBinding clazz = binding.getDeclaringClass();
				clazz.getName();
				IPackageBinding package1 = clazz.getPackage();
				package1.getName();
			}
			return super.visit(node);
		}

		@Override
		public boolean visit(MethodInvocation node) {
			IMethodBinding binding = node.resolveMethodBinding();
			if (binding != null) {
				ITypeBinding[] parameterTypes = binding.getParameterTypes();
				for (ITypeBinding t : parameterTypes) 
					t.getQualifiedName();
				ITypeBinding clazz = binding.getDeclaringClass();
				clazz.getName();
				IPackageBinding package1 = clazz.getPackage();
				package1.getName();
			}
			return super.visit(node);
		}

		@Override
		public boolean visit(ClassInstanceCreation node) {
			IMethodBinding binding = node.resolveConstructorBinding();
			if (binding != null) {
				ITypeBinding[] parameterTypes = binding.getParameterTypes();
				for (ITypeBinding t : parameterTypes) 
					t.getQualifiedName();
				ITypeBinding clazz = binding.getDeclaringClass();
				clazz.getName();
				IPackageBinding package1 = clazz.getPackage();
				package1.getName();
			}
			return super.visit(node);
		}
	}
}
