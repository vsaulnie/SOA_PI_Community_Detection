package insa.sdbd.services.giraph.dockercom;

import java.util.LinkedList;
import java.util.List;

public class DockerExec implements ProcessCommand {
	private String unixUser;
	private boolean detached=false;
	private List<String> cmd;
	private String container;
	public static String HADOOP_HOME = "/usr/local/hadoop";
	public DockerExec(String unixUser, List<String> cmd, String container) {
		this.unixUser = unixUser;
		this.cmd = cmd;
		this.container = container;
	}
	public DockerExec(String unixUser, List<String> cmd, String container, boolean detached) {
		this.unixUser = unixUser;
		this.cmd = cmd;
		this.container = container;
		this.detached = detached;
	}

	public List<String> getCmd(){
		List<String> res = new LinkedList<>();
		res.add("sudo");
		res.add("docker");
		res.add("exec");
		if(detached)res.add("-d");
		if(!unixUser.equals("root")){
			res.add("-u");
			res.add(unixUser);
		}
		res.add("-e");
		res.add("HADOOP_HOME="+HADOOP_HOME);
		res.add(container);
		res.add("bash");
		res.addAll(cmd);
		return res;
	}
}
