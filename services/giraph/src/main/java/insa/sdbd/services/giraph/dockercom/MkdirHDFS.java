package insa.sdbd.services.giraph.dockercom;

import java.util.LinkedList;
import java.util.List;

public class MkdirHDFS implements ProcessCommand {
	private String user;


	public MkdirHDFS(String user) {
		this.user = user;
	}

	public List<String> getCmd(){
		List<String> res = new LinkedList<>();
		res.add("hadoop");
		res.add("fs");
		res.add("-mkdir");
		res.add("-p");
		res.add("/user/hduser/" + user);
		return res;
	}
}
