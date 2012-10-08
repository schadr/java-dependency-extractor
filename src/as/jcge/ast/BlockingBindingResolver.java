package as.jcge.ast;

import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.IPackageBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;

public class BlockingBindingResolver {
	private boolean locked = false;
	private int delay = 2;
	
	
	public IMethodBinding resolveBinding(MethodDeclaration node) {
		while (!obtainLock()){ waiting(delay); }
		
		IMethodBinding resolveBinding = node.resolveBinding();
		
		releaseLock();
		
		return resolveBinding;
	}

	private void releaseLock() {
		locked = false;
	}

	private synchronized boolean obtainLock() {
		if (locked) return false;
		locked = true;
		return true;
	}

	public IPackageBinding getPackage(ITypeBinding clazz) {
		while(!obtainLock()){ waiting(delay); }
		
		IPackageBinding package1 = clazz.getPackage();
		
		releaseLock();
		
		return package1;
	}

	public IMethodBinding resolveMethodBinding(MethodInvocation node) {
		while(!obtainLock()){ waiting(delay); }
		
		IMethodBinding resolveMethodBinding = node.resolveMethodBinding();
		
		releaseLock();
		
		return resolveMethodBinding;
	}

	public ITypeBinding[] getParameterTypes(IMethodBinding methodBinding) {
		//while (!obtainLock()) { waiting(delay); }		
		
		ITypeBinding[] parameterTypes = methodBinding.getParameterTypes();
		
		releaseLock();
		
		return parameterTypes;
	}

	public String getName(IMethodBinding methodBinding) {
		while (!obtainLock()) { waiting(delay); }
		
		String name = methodBinding.getName();
		
		releaseLock();
		
		return name;
	}

	public ITypeBinding getDeclaringClass(IMethodBinding methodBinding) {
		while(!obtainLock()) { waiting(delay); }
		
		ITypeBinding declaringClass = methodBinding.getDeclaringClass();
		
		releaseLock();
		
		return declaringClass;
	}

	public String getName(ITypeBinding clazz) {
		while(!obtainLock()) { waiting(delay); }
		
		String name = clazz.getName();
		
		releaseLock();
		
		return name;
	}

	public String getName(IPackageBinding pkg) {
		//while (!obtainLock()) { waiting(delay); }
		
		String name = pkg.getName();
		
		releaseLock();
		
		return name;
	}

	public String getQualifiedName(ITypeBinding iTypeBinding) {
		while (!obtainLock()) { waiting(delay); }
		
		String qualifiedName = iTypeBinding.getQualifiedName();
		
		releaseLock();
		
		return qualifiedName;
	}

	public IMethodBinding resolveConstructorBinding(ClassInstanceCreation node) {
		while(!obtainLock()) { waiting(delay); }
		
		IMethodBinding resolveConstructorBinding = node.resolveConstructorBinding();
		
		releaseLock();
		
		return resolveConstructorBinding;
	}
	
	private synchronized void waiting(int time) {
		try {
			this.wait((long)time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
