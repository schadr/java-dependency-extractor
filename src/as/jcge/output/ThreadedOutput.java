package as.jcge.output;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import as.jcge.models.CallGraph;

public class ThreadedOutput {
	private XMLOutput fOut;
	private ArrayBlockingQueue<CallGraph> fQueue;
	private boolean fStop;
	private QueueWorker fWorker = new QueueWorker();
	
	public ThreadedOutput(XMLOutput output) {
		fOut = output;
		fQueue = new ArrayBlockingQueue<CallGraph>(10);
	}
	
	public ThreadedOutput(XMLOutput output, int queueSize) {
		fOut = output;
		fQueue = new ArrayBlockingQueue<CallGraph>(queueSize);
	}
	
	public void add(CallGraph cg) throws InterruptedException {
		while (!fQueue.offer(cg,10,TimeUnit.MILLISECONDS)) {}
	}
	
	public void start(String projectName) throws IOException {
		fOut.startOutput(projectName);
		fStop = false;
		fWorker.start();
	}
	
	public void stop() throws IOException, InterruptedException {
		fStop = true;
		fWorker.join();
		fOut.stopOutput();
	}
	
	class QueueWorker extends Thread {
		public void run() {
			while (!fStop || !fQueue.isEmpty()) {
				try {
					CallGraph cg = fQueue.poll(10, TimeUnit.MILLISECONDS);
					if (cg != null) {
						fOut.output(cg);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ParserConfigurationException e) {
					e.printStackTrace();
				} catch (TransformerException e) {
					e.printStackTrace();
				}				
			}
		}
	}
}
