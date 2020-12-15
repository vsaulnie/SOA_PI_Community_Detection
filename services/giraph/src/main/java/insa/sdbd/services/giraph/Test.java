package insa.sdbd.services.giraph;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Test {
	public static void main(String[] args) throws IOException, InterruptedException {
		ProcessBuilder processBuilder = new ProcessBuilder();
		List<String> commands = new LinkedList<>();
		commands.add("cmd");
		commands.add("/c");
		commands.add("echo");
		commands.add("hey");
		commands.add(">");
		commands.add("test.txt");
		processBuilder.command(commands);
		Process process = processBuilder.start();
		process.waitFor();
	}
}
