package as.jcge.output;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import as.jcge.models.CallGraph;
import as.jcge.models.Method;

public class XMLOutput {
	
	public void startOutput(Writer out, String projectName) throws IOException {
		out.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>");
		out.write("<project name=\""+projectName+"\">");
	}
	
	public void stopOutput(Writer out) throws IOException {
		out.write("</project>");
	}
	
	public void output(CallGraph cg, Writer out) throws ParserConfigurationException, TransformerException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.newDocument();	
		
		// commit node
		Element commitElement = doc.createElement("commit");
		commitElement.setAttribute("commit_id", cg.getCommit().commitID);
		commitElement.setAttribute("author", cg.getCommit().author);
		commitElement.setAttribute("time", cg.getCommit().time.toString());
		doc.appendChild(commitElement);
		
		Collection<Method> methods = cg.getMethods().values();
		for (Method m : methods) {
			List<Method> callees = cg.getCalledMethods(m);
			commitElement.appendChild( createMethodNode(m, callees, doc));
		}
		
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(out);
		transformer.transform(source, result);
	}

	private Element createMethodNode(Method m, List<Method> callees, Document doc) {
		Element methodNode = doc.createElement("method");
		methodNode.setAttribute("fully_quallified_class", m.getPkg() + "." + m.getClazz());
		methodNode.setAttribute("was_changed", String.valueOf(m.wasModified()));
		
		methodNode.appendChild(createParametersNode(m, doc));
		if (callees != null && callees.size() > 0)
			methodNode.appendChild(createCalleesNode(callees, doc));
		
		return methodNode;
	}

	private Element createCalleesNode(List<Method> callees, Document doc) {
		Element calleesNode = doc.createElement("called_methods");
		calleesNode.setAttribute("count", String.valueOf(callees.size()));
		for (Method callee : callees) {
			Element calleeNode = doc.createElement("called_method");
			calleeNode.setAttribute("fully_quallified_class", callee.getPkg() + "." + callee.getClazz());
			calleeNode.appendChild(createParametersNode(callee, doc));
			calleesNode.appendChild(calleeNode);
		}
		return calleesNode;
	}

	private Element createParametersNode(Method m, Document doc) {
		Element parametersNode = doc.createElement("parameters");
		parametersNode.setAttribute("count", String.valueOf(m.getParameters().size()));
		int parameterPosition = 1;
		for (String parameter : m.getParameters()) {
			Element parameterNode = doc.createElement("parameter");
			parameterNode.setAttribute("name", parameter);
			parameterNode.setAttribute("type", parameter);
			parameterNode.setAttribute("position", String.valueOf(parameterPosition));
			parametersNode.appendChild(parameterNode);
			parameterPosition++;
		}
		return parametersNode;
	}
}
