package insa.sdbd.services.giraph.hdprocess;

import insa.sdbd.services.giraph.ProcessCommand;

import java.util.LinkedList;
import java.util.List;

public class PutHDFS implements ProcessCommand {
	private String fileFrom;
	private String fileTo;

	public PutHDFS(String fileFrom, String fileTo) {
		this.fileFrom = fileFrom;
		this.fileTo = fileTo;
	}

	@Override
	public List<String> getCmd() {
		List<String> res = new LinkedList<>();
		res.add("hadoop");
		res.add("fs");
		res.add("-put");
		res.add(fileFrom);
		res.add(fileTo);
		return res;
	}
}
