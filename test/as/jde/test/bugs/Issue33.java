/**
 * link to bug report
 * https://github.com/schadr/java-dependency-extractor/issues/33
 */
package as.jde.test.bugs;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.Writer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import as.jcge.main.ArgumentParser;
import as.jcge.output.ThreadedOutput;
import as.jcge.output.XMLOutput;
import as.jcge.scm.SCMIterator;
import as.jcge.scm.git.GitController;
import as.jde.test.util.RepositoryManager;

public class Issue33 {
	private String repoPath = null;

	@Before
	public void setup() {
		RepositoryManager rm = RepositoryManager.getInstance();
		rm.createSelfRepository();
		repoPath = RepositoryManager.SELF_REPO_PATH;
	}

	@After
	public void tearDown() {

	}

	@Test
	public void test() throws InterruptedException, IOException {
		GitController git = new GitController(repoPath);
		SCMIterator iter = new SCMIterator(git, ArgumentParser.IGNORE_FOLDER_DEFAULT);
		ThreadedOutput out = new ThreadedOutput(new XMLOutput(new Writer() {

			@Override
			public void close() throws IOException {}

			@Override
			public void flush() throws IOException {}

			@Override
			public void write(char[] arg0, int arg1, int arg2) throws IOException {}
		}));

		out.start("self");
		
		try {
			while (iter.hasNext()) {
				out.add(iter.next());
			}
		} catch (IllegalStateException e) {
			if (e.getMessage().equals("Queue full")) {
				fail("Issue 33");
			} else {
				throw e;
			}
		}
		
		out.stop();
	}

}
