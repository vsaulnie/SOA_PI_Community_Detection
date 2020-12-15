package insa.sdbd.services.giraph.dockercom;

import java.io.IOException;
import java.util.List;

public interface ProcessCommand {
	List<String> getCmd();
	default String repr(){
		return String.join(" ",getCmd());
	}
	default int runProcess() throws IOException, InterruptedException {
		ProcessBuilder processBuilder = new ProcessBuilder();
		processBuilder.command(getCmd());
		Process process = processBuilder.start();
		return process.waitFor();
	}
}
