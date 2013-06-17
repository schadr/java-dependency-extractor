package as.jde.test.main;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import as.jde.main.ArgumentParser;

public class ArgumentParserTest {

	@Test
	public void shouldParseRepositoryLong() {
		CommandLineArgumentBuilder builder = new CommandLineArgumentBuilder();
		String[] args = builder.addRepositoryLong()
							   .build();
		
		Map<String, String> arguments = ArgumentParser.parseArguments(args);
		
		assertTrue(arguments.containsKey(ArgumentParser.OPT_REPOSITORY));
	}

	@Test
	public void shouldParseRepositoryShort() {
		CommandLineArgumentBuilder builder = new CommandLineArgumentBuilder();
		String[] args = builder.addRepositoryShort()
							   .build();
		
		Map<String, String> arguments = ArgumentParser.parseArguments(args);
		
		assertTrue(arguments.containsKey(ArgumentParser.OPT_REPOSITORY));
	}
	
	@Test
	public void shouldParseOutputFormatLong() {
		CommandLineArgumentBuilder builder = new CommandLineArgumentBuilder();
		String[] args = builder.addOutputFormatLong()
							   .build();
		
		Map<String, String> arguments = ArgumentParser.parseArguments(args);
		
		assertTrue(arguments.containsKey(ArgumentParser.OPT_OUTPUT_FORMAT));
	}

	@Test
	public void shouldParseOutputFormatShort() {
		CommandLineArgumentBuilder builder = new CommandLineArgumentBuilder();
		String[] args = builder.addOutputFormatShort()
							   .build();
		
		Map<String, String> arguments = ArgumentParser.parseArguments(args);
		
		assertTrue(arguments.containsKey(ArgumentParser.OPT_OUTPUT_FORMAT));
	}
	
	@Test
	public void shouldParseIgnoreFolderLong() {
		CommandLineArgumentBuilder builder = new CommandLineArgumentBuilder();
		String[] args = builder.addIgnoreFolderLong()
							   .build();
		
		Map<String, String> arguments = ArgumentParser.parseArguments(args);
		
		assertTrue(arguments.containsKey(ArgumentParser.OPT_IGNORE_FOLDER));
	}

	@Test
	public void shouldParseIgnoreFolderShort() {
		CommandLineArgumentBuilder builder = new CommandLineArgumentBuilder();
		String[] args = builder.addIgnoreFolderShort()
							   .build();
		
		Map<String, String> arguments = ArgumentParser.parseArguments(args);
		
		assertTrue(arguments.containsKey(ArgumentParser.OPT_IGNORE_FOLDER));
	}
	
	@Test
	public void shouldParseQueueLimitLong() {
		CommandLineArgumentBuilder builder = new CommandLineArgumentBuilder();
		String[] args = builder.addQueueLimitLong()
							   .build();
		
		Map<String, String> arguments = ArgumentParser.parseArguments(args);
		
		assertTrue(arguments.containsKey(ArgumentParser.OPT_QUEUE_LIMIT));
	}

	@Test
	public void shouldParseQueueLimitShort() {
		CommandLineArgumentBuilder builder = new CommandLineArgumentBuilder();
		String[] args = builder.addQueueLimitShort()
							   .build();
		
		Map<String, String> arguments = ArgumentParser.parseArguments(args);
		
		assertTrue(arguments.containsKey(ArgumentParser.OPT_QUEUE_LIMIT));
	}
	
	@Test
	public void shouldParseTempFolderLong() {
		CommandLineArgumentBuilder builder = new CommandLineArgumentBuilder();
		String[] args = builder.addTempFolderLong()
							   .build();
		
		Map<String, String> arguments = ArgumentParser.parseArguments(args);
		
		assertTrue(arguments.containsKey(ArgumentParser.OPT_TMP_FOLDER));
	}

	@Test
	public void shouldParseTempFolderShort() {
		CommandLineArgumentBuilder builder = new CommandLineArgumentBuilder();
		String[] args = builder.addTempFolderShort()
							   .build();
		
		Map<String, String> arguments = ArgumentParser.parseArguments(args);
		
		assertTrue(arguments.containsKey(ArgumentParser.OPT_TMP_FOLDER));
	}
	
	@Test
	public void shouldParseNumberOfTempCopiesLong() {
		CommandLineArgumentBuilder builder = new CommandLineArgumentBuilder();
		String[] args = builder.addNumberOfTempCopiesLong()
							   .build();
		
		Map<String, String> arguments = ArgumentParser.parseArguments(args);
		
		assertTrue(arguments.containsKey(ArgumentParser.OPT_TMP_COPIES));
	}

	@Test
	public void shouldParseNumberOfTempCopiesShort() {
		CommandLineArgumentBuilder builder = new CommandLineArgumentBuilder();
		String[] args = builder.addNumberOfTempCopiesShort()
							   .build();
		
		Map<String, String> arguments = ArgumentParser.parseArguments(args);
		
		assertTrue(arguments.containsKey(ArgumentParser.OPT_TMP_COPIES));
	}
	
	@Test
	public void shouldParseEverythingShort() {
		CommandLineArgumentBuilder builder = new CommandLineArgumentBuilder();
		String[] args = builder.addNumberOfTempCopiesShort()
				.addIgnoreFolderShort()
				.addNumberOfTempCopiesShort()
				.addOutputFormatShort()
				.addQueueLimitShort()
				.addRepositoryShort()
				.addTempFolderShort()
				.build();
		
		Map<String, String> arguments = ArgumentParser.parseArguments(args);
		
		assertTrue(arguments.containsKey(ArgumentParser.OPT_TMP_COPIES));
		assertTrue(arguments.containsKey(ArgumentParser.OPT_TMP_FOLDER));
		assertTrue(arguments.containsKey(ArgumentParser.OPT_QUEUE_LIMIT));
		assertTrue(arguments.containsKey(ArgumentParser.OPT_IGNORE_FOLDER));
		assertTrue(arguments.containsKey(ArgumentParser.OPT_OUTPUT_FORMAT));
		assertTrue(arguments.containsKey(ArgumentParser.OPT_REPOSITORY));
	}
}

class CommandLineArgumentBuilder {
	private List<String> arguments = new ArrayList<String>();
	
	public String[] build() {
		return arguments.toArray(new String[]{});
	}

	public CommandLineArgumentBuilder addNumberOfTempCopiesShort() {
		arguments.add(ArgumentParser.OPT_TMP_COPIES_SHORT);
		arguments.add(ArgumentParser.TMP_COPIES_DEFAULT);
		return this;
	}

	public CommandLineArgumentBuilder addNumberOfTempCopiesLong() {
		arguments.add(ArgumentParser.OPT_TMP_COPIES + "=" + ArgumentParser.TMP_COPIES_DEFAULT);
		return this;
	}

	public CommandLineArgumentBuilder addTempFolderShort() {
		arguments.add(ArgumentParser.OPT_TMP_FOLDER_SHORT);
		arguments.add("/tmp");
		return this;
	}

	public CommandLineArgumentBuilder addTempFolderLong() {
		arguments.add(ArgumentParser.OPT_TMP_FOLDER + "=/tmp");
		return this;
	}

	public CommandLineArgumentBuilder addQueueLimitShort() {
		arguments.add(ArgumentParser.OPT_QUEUE_LIMIT_SHORT);
		arguments.add(ArgumentParser.QUEUE_LIMIT_DEFAULT);
		return this;
	}

	public CommandLineArgumentBuilder addQueueLimitLong() {
		arguments.add(ArgumentParser.OPT_QUEUE_LIMIT + "=" + ArgumentParser.QUEUE_LIMIT_DEFAULT);
		return this;
	}

	public CommandLineArgumentBuilder addIgnoreFolderShort() {
		arguments.add(ArgumentParser.OPT_IGNORE_FOLDER_SHORT);
		arguments.add(ArgumentParser.IGNORE_FOLDER_DEFAULT);
		return this;
	}

	public CommandLineArgumentBuilder addIgnoreFolderLong() {
		arguments.add(ArgumentParser.OPT_IGNORE_FOLDER + "=" + ArgumentParser.IGNORE_FOLDER_DEFAULT);
		return this;
	}

	public CommandLineArgumentBuilder addOutputFormatShort() {
		arguments.add(ArgumentParser.OPT_OUTPUT_FORMAT_SHORT);
		arguments.add(ArgumentParser.OUTPUT_XML);
		return this;
	}

	public CommandLineArgumentBuilder addOutputFormatLong() {
		arguments.add(ArgumentParser.OPT_OUTPUT_FORMAT + "=" + ArgumentParser.OUTPUT_XML);
		return this;
	}

	public CommandLineArgumentBuilder addRepositoryShort() {
		arguments.add(ArgumentParser.OPT_REPOSITORY_SHORT);
		arguments.add(ArgumentParser.REPOSITORY_GIT);
		return this;
	}

	public CommandLineArgumentBuilder addRepositoryLong() {
		arguments.add(ArgumentParser.OPT_REPOSITORY + "=" + ArgumentParser.REPOSITORY_GIT);
		return this;
	}
}
