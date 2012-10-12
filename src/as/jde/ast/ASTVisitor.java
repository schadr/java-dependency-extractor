package as.jde.ast;

import java.util.Stack;

import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.IPackageBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;

import as.jde.graph.CallGraph;
import as.jde.graph.Method;

public class ASTVisitor extends org.eclipse.jdt.core.dom.ASTVisitor {
	
	private Stack<Method> methodStack;
	private String file;
	CompilationUnit cu;
	CallGraph cg;
	private BlockingBindingResolver bbr;
	
	public ASTVisitor(String file, CompilationUnit cu, CallGraph cg, BlockingBindingResolver bbr) {
		methodStack = new Stack<Method>();
		this.file = file;
		this.cg = cg;
		this.cu = cu;
		this.bbr = bbr;
	}
	
	/**
	 * This function overrides what to do when we reach
	 * a method declaration.
	 */
	@Override
	public boolean visit(MethodDeclaration node) {
		// Insert the method into the DB
		IMethodBinding methodBinding = bbr.resolveBinding(node);
		Method method = createMethodFromBinding(node, methodBinding);
		
		if(method != null) {
			// Insert
			cg.addMethod(method);

			// Push onto stack
			methodStack.push(method);
		}
		
		return super.visit(node);
	}
	
	/**
	 * This function overrides what to do when we reach
	 * the end of a method declaration.
	 */
	@Override
	public void endVisit(MethodDeclaration node) {
		if(!methodStack.isEmpty())
			methodStack.pop();
	}
	
	/**
	 * This function overrides what to do when we reach
	 * a method invocation statement.
	 */
	@Override
	public boolean visit(MethodInvocation node) {
		// Insert the method into the DB
		IMethodBinding methodBinding = bbr.resolveMethodBinding(node);
		Method method = createMethodFromBinding(null, methodBinding);
		
		// Insert
		if(method != null) {
			cg.addMethod(method);
			if(!methodStack.isEmpty())
				cg.addInvokes(methodStack.peek(), method);
		}
		
		return super.visit(node);
	}
	
	/**
	 * This function overrides what to do when we reach
	 * a constructor invocation.
	 */
	@Override
	public boolean visit(ClassInstanceCreation node) {
		// Resolve the constructor
		IMethodBinding methodBinding = bbr.resolveConstructorBinding(node);
		Method method = createMethodFromBinding(null, methodBinding);
		
		// Insert
		if(method != null) {
			cg.addMethod(method);
			if(!methodStack.isEmpty())
				cg.addInvokes(methodStack.peek(), method);
		}
		
		return super.visit(node);
	}
	
	private Method createMethodFromBinding(MethodDeclaration node, IMethodBinding methodBinding) {
		if(methodBinding != null) {
			Method method = new Method();
			method.setName(bbr.getName(methodBinding));
			if(node != null) {
				// Handle working directory
				method.setFile(file);
				method.setStart(cu.getLineNumber(node.getStartPosition()));
				method.setEnd(cu.getLineNumber(node.getStartPosition() + node.getLength()));
			}
			
			// Parameters
			ITypeBinding[] parameters = bbr.getParameterTypes(methodBinding);
			if(parameters.length > 0) {
				for(int i = 0; i < parameters.length; i++) {
					if(parameters[i] != null)
						method.addParameter(bbr.getQualifiedName(parameters[i]));
				}
			}
			
			// Class and Package
			ITypeBinding clazz = bbr.getDeclaringClass(methodBinding);
			if(clazz != null) {
				method.setClazz(bbr.getName(clazz));
				
				IPackageBinding pkg = bbr.getPackage(clazz);
				if(pkg != null)
					method.setPkg(bbr.getName(pkg));
			}
			
			return method;
		}
		else
			return null;
	}
}
