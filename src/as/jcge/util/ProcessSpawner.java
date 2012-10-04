package as.jcge.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class ProcessSpawner {
	private File workingDirectory;
	
	public ProcessSpawner() {
	}
	
	/**
	 * Set the working directory of processes spawned.
	 * @param wd
	 */
	public ProcessSpawner(String wd) {
		workingDirectory = new File(wd);
	}
	
	/**
	 * Spawns the desired process and return the output as
	 * a String.
	 * @param command
	 * @return
	 */
	public String spawnProcess(String[] command) {
		ProcessBuilder processBuilder = new ProcessBuilder(command);
		processBuilder.directory(workingDirectory);
		processBuilder.redirectErrorStream(true);
		Process process;
		try {
			process = processBuilder.start();
		}
		catch(IOException e) {
			return null;
		}
		
		InputStream is = process.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		
		String line;
		StringBuilder builder = new StringBuilder();
		
		try {
			while ((line = br.readLine()) != null) {
				builder.append(line);
				builder.append(System.getProperty("line.separator"));
				//output += line + System.getProperty("line.separator");
			}
		}
		catch(Exception e) {
			process.destroy();
			e.printStackTrace();
		}
		
		//return output;
		return builder.toString();
	}
	
	/**
	 * Spawns the desired process and return the output as
	 * a String.
	 * @param command
	 * @return
	 */
	public String spawnProcess(List<String> command) {
		ProcessBuilder processBuilder = new ProcessBuilder(command);
		processBuilder.directory(workingDirectory);
		processBuilder.redirectErrorStream(true);
		Process process;
		try {
			process = processBuilder.start();
		}
		catch(IOException e) {
			return null;
		}
		
		InputStream is = process.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		
		String line;
		String output = "";
		
		try {
			while ((line = br.readLine()) != null) {
				output += line + System.getProperty("line.separator");
			}
		}
		catch(Exception e) {
			process.destroy();
			e.printStackTrace();
		}
		
		return output;
	}
}
