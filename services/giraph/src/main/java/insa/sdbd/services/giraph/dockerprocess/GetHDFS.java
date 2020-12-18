package insa.sdbd.services.giraph.dockerprocess;

import java.util.LinkedList;
import java.util.List;

public class GetHDFS {
	private String srcHDFS;
	private String destLocal;

	public GetHDFS(String srcHdfs, String destLocal) {
		this.srcHDFS = srcHdfs;
		this.destLocal = destLocal;
	}

	public List<String> getCmd(){
		List<String> res = new LinkedList<>();
		res.add("hadoop");
		res.add("fs");
		res.add("-copyToLocal");
		res.add(srcHDFS);
		res.add(destLocal);
		return res;
	}
}
