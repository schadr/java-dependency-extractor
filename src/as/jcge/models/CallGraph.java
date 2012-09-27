package as.jcge.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CallGraph {
	private Map<String, Method> methods;
	private Map<Method, List<Method>> invokes;

	public CallGraph() {
		methods = new HashMap<String, Method>();
		invokes = new HashMap<Method, List<Method>>();
	}

	public CallGraph(Map<String, Method> methods, Map<Method, List<Method>> invokes) {
		this.methods = methods;
		this.invokes = invokes;
	}
	
	public void addMethod(Method method) {
		if(methods.containsKey(method.toString())) {
			// Update
			if(method.getStart() != -1 && method.getEnd() != -1) {
				Method value = methods.get(method.toString());
				value.setFile(method.getFile());
				value.setStart(method.getStart());
				value.setEnd(method.getEnd());
			}
		}
		else {
			// Insert
			methods.put(method.toString(), method);
		}
	}
	
	public void addInvokes(Method caller, Method callee) {
		if(invokes.containsKey(caller)) {
			List<Method> calls = invokes.get(caller);
			calls.add(callee);
		}
		else {
			List<Method> calls = new ArrayList<Method>();
			calls.add(callee);
			invokes.put(caller, calls);
		}
	}
	
	public List<Pair<Method, Float>> getChangedMethods(String file, int start, int end) {
		List<Pair<Method, Float>> changedMethods = new ArrayList<Pair<Method, Float>>();
		
		for(Map.Entry<String, Method> entry: methods.entrySet()) {
			Method method = entry.getValue();
			if(method.getFile() == null)
				continue;
			if(method.getFile().equals(file) &&
					(Math.min(end, method.getEnd()) - Math.max(start, method.getStart()) >= 0)) {
				Pair<Method, Float> pair = new Pair<Method, Float>(method, 0.0f);
				// Add real weight
				pair.setSecond((Math.min(end, pair.getFirst().getEnd()) - 
						Math.max(start, pair.getFirst().getStart()) + 1)/
						(float)(pair.getFirst().getEnd() - pair.getFirst().getStart() + 1));
				
				changedMethods.add(pair);
			}
		}
		
		return changedMethods;
	}
	
	public List<Method> getCallersOfMethod(Method method) {
		List<Method> callers = new ArrayList<Method>();
		
		for(Map.Entry<Method, List<Method>> entry: invokes.entrySet()) {
			List<Method> callees = entry.getValue();
			for(Method callee: callees) {
				if(callee.toString().equals(method.toString())) {
					callers.add(entry.getKey());
					break;
				}
			}
		}
		
		return callers;
	}

	public Map<String, Method> getMethods() {
		return methods;
	}

	public void setMethods(Map<String, Method> methods) {
		this.methods = methods;
	}

	public Map<Method, List<Method>> getInvokes() {
		return invokes;
	}

	public void setInvokes(Map<Method, List<Method>> invokes) {
		this.invokes = invokes;
	}
	
	
}
