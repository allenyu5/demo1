package per.allen;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.CreateBranchCommand.SetupUpstreamMode;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.errors.AmbiguousObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.NoWorkTreeException;
import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

	@Test
	public void contextLoads() {
	}

	private String localPath, remotePath;
	private Repository localRepo;
	private Git git;

	@Before
	public void init() throws IOException {
		localPath = "./src/repo";
		remotePath = "git@github.com:allenyu5/demo1.git";
		localRepo = new FileRepository(localPath + "/.git");
		git = new Git(localRepo);
	}

	// @Test
	// public void testCreate() throws IOException {
	// Repository newRepo = new FileRepository(localPath + ".git");
	// newRepo.create();
	// }

	@Test
	public void testClone() throws IOException, GitAPIException {
		Git.cloneRepository().setURI(remotePath).setDirectory(new File(localPath)).call();
	}

	@Test
	public void testAdd() throws IOException, GitAPIException {
		File myfile = new File(localPath + "/myfile");
		myfile.createNewFile();
		git.add().addFilepattern("myfile").call();
	}

	@Test
	public void testCommit() throws IOException, GitAPIException, JGitInternalException {
		git.commit().setMessage("Added my test file").call();
	}

	@Test
	public void testPush() throws IOException, JGitInternalException, GitAPIException {
		git.push().call();
	}

	@Test // what is this testcase for, seems no idea
	public void testTrackMaster() throws IOException, JGitInternalException, GitAPIException {
		git.branchCreate().setName("master").setUpstreamMode(SetupUpstreamMode.SET_UPSTREAM)
				.setStartPoint("origin/master").setForce(true).call();
	}

	@Test
	public void testPull() throws IOException, GitAPIException {
		git.pull().call();
		// git.pull().setRemote(remotePath).call();
	}

	@Test
	public void testLog() throws RevisionSyntaxException, AmbiguousObjectException, IncorrectObjectTypeException,
			IOException, NoHeadException, GitAPIException {
		ObjectId head = localRepo.resolve(Constants.HEAD);
		Iterable<RevCommit> commits = git.log().add(head).call();

		for (RevCommit iterable : commits) {
			System.out.println(iterable.toString());
			System.out.println(iterable.getAuthorIdent());
			System.out.println(iterable.getFullMessage());
		}
	}

	@Test
	public void testStatus() throws NoWorkTreeException, GitAPIException {
		Status status = git.status().call();
		System.out.println("Added: " + status.getAdded());
		System.out.println("Changed: " + status.getChanged());
		System.out.println("Conflicting: " + status.getConflicting());
		System.out.println("ConflictingStageState: " + status.getConflictingStageState());
		System.out.println("IgnoredNotInIndex: " + status.getIgnoredNotInIndex());
		System.out.println("Missing: " + status.getMissing());
		System.out.println("Modified: " + status.getModified());
		System.out.println("Removed: " + status.getRemoved());
		System.out.println("Untracked: " + status.getUntracked());
		System.out.println("UntrackedFolders: " + status.getUntrackedFolders());

	}

	@Test
	public void testCheckout() {
		git.checkout().setName("");
	}

}
