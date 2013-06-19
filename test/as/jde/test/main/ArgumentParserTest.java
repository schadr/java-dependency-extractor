package as.jde.test.main;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import as.jde.main.ArgumentParser;
import as.jde.main.input.IgnoreFolderArgument;
import as.jde.main.input.OutputArgument;
import as.jde.main.input.QueueLimitArgument;
import as.jde.main.input.RepositoryArgument;
import as.jde.main.input.TempCopiesArgument;
import as.jde.main.input.TempFolderArgument;

public class ArgumentParserTest {
	
	private ArgumentParser argumentParser;

	@Before
	public void before() {
		argumentParser = new ArgumentParser();
	}

	@Test
	public void shouldParseRepositoryLong() {
		CommandLineArgumentBuilder builder = new CommandLineArgumentBuilder();
		String[] args = builder.addRepositoryLong()
							   .build();
		
		Map<String, String> arguments = argumentParser.parseArguments(args);
		
		assertTrue(arguments.containsKey(RepositoryArgument.OPT_REPOSITORY));
	}

	@Test
	public void shouldParseRepositoryShort() {
		CommandLineArgumentBuilder builder = new CommandLineArgumentBuilder();
		String[] args = builder.addRepositoryShort()
							   .build();
		
		Map<String, String> arguments = argumentParser.parseArguments(args);
		
		assertTrue(arguments.containsKey(RepositoryArgument.OPT_REPOSITORY));
	}
	
	@Test
	public void shouldParseOutputFormatLong() {
		CommandLineArgumentBuilder builder = new CommandLineArgumentBuilder();
		String[] args = builder.addOutputFormatLong()
							   .build();
		
		Map<String, String> arguments = argumentParser.parseArguments(args);
		
		assertTrue(arguments.containsKey(OutputArgument.OPT_OUTPUT_FORMAT));
	}

	@Test
	public void shouldParseOutputFormatShort() {
		CommandLineArgumentBuilder builder = new CommandLineArgumentBuilder();
		String[] args = builder.addOutputFormatShort()
							   .build();
		
		Map<String, String> arguments = argumentParser.parseArguments(args);
		
		assertTrue(arguments.containsKey(OutputArgument.OPT_OUTPUT_FORMAT));
	}
	
	@Test
	public void shouldParseIgnoreFolderLong() {
		CommandLineArgumentBuilder builder = new CommandLineArgumentBuilder();
		String[] args = builder.addIgnoreFolderLong()
							   .build();
		
		Map<String, String> arguments = argumentParser.parseArguments(args);
		
		assertTrue(arguments.containsKey(IgnoreFolderArgument.OPT_IGNORE_FOLDER));
	}

	@Test
	public void shouldParseIgnoreFolderShort() {
		CommandLineArgumentBuilder builder = new CommandLineArgumentBuilder();
		String[] args = builder.addIgnoreFolderShort()
							   .build();
		
		Map<String, String> arguments = argumentParser.parseArguments(args);
		
		assertTrue(arguments.containsKey(IgnoreFolderArgument.OPT_IGNORE_FOLDER));
	}
	
	@Test
	public void shouldParseQueueLimitLong() {
		CommandLineArgumentBuilder builder = new CommandLineArgumentBuilder();
		String[] args = builder.addQueueLimitLong()
							   .build();
		
		Map<String, String> arguments = argumentParser.parseArguments(args);
		
		assertTrue(arguments.containsKey(QueueLimitArgument.OPT_QUEUE_LIMIT));
	}

	@Test
	public void shouldParseQueueLimitShort() {
		CommandLineArgumentBuilder builder = new CommandLineArgumentBuilder();
		String[] args = builder.addQueueLimitShort()
							   .build();
		
		Map<String, String> arguments = argumentParser.parseArguments(args);
		
		assertTrue(arguments.containsKey(QueueLimitArgument.OPT_QUEUE_LIMIT));
	}
	
	@Test
	public void shouldParseTempFolderLong() {
		CommandLineArgumentBuilder builder = new CommandLineArgumentBuilder();
		String[] args = builder.addTempFolderLong()
							   .build();
		
		Map<String, String> arguments = argumentParser.parseArguments(args);
		
		assertTrue(arguments.containsKey(TempFolderArgument.OPT_TMP_FOLDER));
	}

	@Test
	public void shouldParseTempFolderShort() {
		CommandLineArgumentBuilder builder = new CommandLineArgumentBuilder();
		String[] args = builder.addTempFolderShort()
							   .build();
		
		Map<String, String> arguments = argumentParser.parseArguments(args);
		
		assertTrue(arguments.containsKey(TempFolderArgument.OPT_TMP_FOLDER));
	}
	
	@Test
	public void shouldParseNumberOfTempCopiesLong() {
		CommandLineArgumentBuilder builder = new CommandLineArgumentBuilder();
		String[] args = builder.addNumberOfTempCopiesLong()
							   .build();
		
		Map<String, String> arguments = argumentParser.parseArguments(args);
		
		assertTrue(arguments.containsKey(TempCopiesArgument.OPT_TMP_COPIES));
	}

	@Test
	public void shouldParseNumberOfTempCopiesShort() {
		CommandLineArgumentBuilder builder = new CommandLineArgumentBuilder();
		String[] args = builder.addNumberOfTempCopiesShort()
							   .build();
		
		Map<String, String> arguments = argumentParser.parseArguments(args);
		
		assertTrue(arguments.containsKey(TempCopiesArgument.OPT_TMP_COPIES));
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
		
		Map<String, String> arguments = argumentParser.parseArguments(args);
		
		assertTrue(arguments.containsKey(TempCopiesArgument.OPT_TMP_COPIES));
		assertTrue(arguments.containsKey(TempFolderArgument.OPT_TMP_FOLDER));
		assertTrue(arguments.containsKey(QueueLimitArgument.OPT_QUEUE_LIMIT));
		assertTrue(arguments.containsKey(IgnoreFolderArgument.OPT_IGNORE_FOLDER));
		assertTrue(arguments.containsKey(OutputArgument.OPT_OUTPUT_FORMAT));
		assertTrue(arguments.containsKey(RepositoryArgument.OPT_REPOSITORY));
	}
}

class CommandLineArgumentBuilder {
	private List<String> arguments = new ArrayList<String>();
	
	public String[] build() {
		return arguments.toArray(new String[]{});
	}

	public CommandLineArgumentBuilder addNumberOfTempCopiesShort() {
		arguments.add(TempCopiesArgument.OPT_TMP_COPIES_SHORT);
		arguments.add(TempCopiesArgument.DEFAULT);
		return this;
	}

	public CommandLineArgumentBuilder addNumberOfTempCopiesLong() {
		arguments.add(TempCopiesArgument.OPT_TMP_COPIES + "=" + TempCopiesArgument.DEFAULT);
		return this;
	}

	public CommandLineArgumentBuilder addTempFolderShort() {
		arguments.add(TempFolderArgument.OPT_TMP_FOLDER_SHORT);
		arguments.add("/tmp");
		return this;
	}

	public CommandLineArgumentBuilder addTempFolderLong() {
		arguments.add(TempFolderArgument.OPT_TMP_FOLDER + "=/tmp");
		return this;
	}

	public CommandLineArgumentBuilder addQueueLimitShort() {
		arguments.add(QueueLimitArgument.OPT_QUEUE_LIMIT_SHORT);
		arguments.add(QueueLimitArgument.DEFAULT);
		return this;
	}

	public CommandLineArgumentBuilder addQueueLimitLong() {
		arguments.add(QueueLimitArgument.OPT_QUEUE_LIMIT + "=" + QueueLimitArgument.DEFAULT);
		return this;
	}

	public CommandLineArgumentBuilder addIgnoreFolderShort() {
		arguments.add(IgnoreFolderArgument.OPT_IGNORE_FOLDER_SHORT);
		arguments.add(IgnoreFolderArgument.DEFAULT);
		return this;
	}

	public CommandLineArgumentBuilder addIgnoreFolderLong() {
		arguments.add(IgnoreFolderArgument.OPT_IGNORE_FOLDER + "=" + IgnoreFolderArgument.DEFAULT);
		return this;
	}

	public CommandLineArgumentBuilder addOutputFormatShort() {
		arguments.add(OutputArgument.OPT_OUTPUT_FORMAT_SHORT);
		arguments.add(OutputArgument.DEFAULT);
		return this;
	}

	public CommandLineArgumentBuilder addOutputFormatLong() {
		arguments.add(OutputArgument.OPT_OUTPUT_FORMAT + "=" + OutputArgument.DEFAULT);
		return this;
	}

	public CommandLineArgumentBuilder addRepositoryShort() {
		arguments.add(RepositoryArgument.OPT_REPOSITORY_SHORT);
		arguments.add(RepositoryArgument.DEFAULT);
		return this;
	}

	public CommandLineArgumentBuilder addRepositoryLong() {
		arguments.add(RepositoryArgument.OPT_REPOSITORY + "=" + RepositoryArgument.DEFAULT);
		return this;
	}
}
