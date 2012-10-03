/**
 * This test case is 
 */
package as.jcge.bugs.npe;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import as.jcge.scm.SCMIterator;
import as.jcge.scm.git.GitController;
import as.jcge.util.ProcessSpawner;

public class HibernateNPE22 {
	private final static String REPOPATH = "./test-repo";
	
	@BeforeClass
    public static void oneTimeSetUp() {
        // get the hibernate-orm repository
		System.err.println("Cloning hibernate-orm");
		ProcessSpawner s = new ProcessSpawner();
		s.spawnProcess(new String[] {"git","clone","git://github.com/hibernate/hibernate-orm.git",REPOPATH});
		System.err.println("DONE cloning");
    }
	
	@AfterClass
	public static void oneTimeTearDown() {
		// delete the hibernate-orm repository
		ProcessSpawner s = new ProcessSpawner();
		s.spawnProcess(new String[] {"rm", "-rf", REPOPATH});
	}
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		GitController gc = new MockGitController(REPOPATH);
		
		try {
			SCMIterator iter = new SCMIterator(gc);
			while (iter.hasNext()) {
				iter.next();
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
			fail();
		}
	}

	class MockGitController extends GitController {

		public MockGitController(String repositoryPath) {
			super(repositoryPath);
		}

		public List<String> getAllCommits() {
			List<String> commits = new ArrayList<String>();
			commits.add("469669f121671a9f733d426fbbeddd4016f769ef");
			commits.add("f8c4d0252e277938343ac18aca597df18a631d02");
			commits.add("50da726269a5cd9b722cb44a6f40f0a5ca1e0e91");
			commits.add("6143e94e84f976a46e0805169383162e4c1cc6be");
			commits.add("dd2e0188d494b4e7d4d25a95b42787c5cf67d3f0");
			commits.add("0f42598a04447d15e6e1d1f32d6a2e1011e452da");
			commits.add("143a2402d5aebbcadc42e118903ce2a3d6fef0c3");
			commits.add("1f8720df0a206d5815dfa90db21ba547eee3a908");
			commits.add("5a055acb2b8c277aa2bed9ac4179884f74f60672");
			return commits;
		}
	}
}
