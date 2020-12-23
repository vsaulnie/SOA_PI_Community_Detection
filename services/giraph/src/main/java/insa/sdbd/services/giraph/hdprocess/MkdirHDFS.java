package insa.sdbd.services.giraph.hdprocess;

import insa.sdbd.services.giraph.ProcessCommand;

import java.util.LinkedList;
import java.util.List;

public class MkdirHDFS implements ProcessCommand {
	private String userStore;


	public MkdirHDFS(String userStore) {
		this.userStore = userStore;
	}

	public List<String> getCmd(){
		List<String> res = new LinkedList<>();
		res.add("hadoop");
		res.add("fs");
		res.add("-mkdir");
		res.add("/user/hduser/" + userStore);
		return res;
	}
}
