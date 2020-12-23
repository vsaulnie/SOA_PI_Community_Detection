package insa.sdbd.services.giraph.hdprocess;

import java.util.LinkedList;
import java.util.List;

public class PutHDFS {
	private String srcLocal;
	private String destHDFS;

	public PutHDFS(String srcLocal, String destHDFS) {
		this.srcLocal = srcLocal;
		this.destHDFS = destHDFS;
	}

	public List<String> getCmd(){
		List<String> res = new LinkedList<>();
		res.add("hadoop");
		res.add("fs");
		res.add("-put");
		res.add(srcLocal);
		res.add(destHDFS);
		return res;
	}
}
