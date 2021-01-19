package insa.sdbd.community.launcher;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GiraphFileBuilder {
	HashMap<Integer, List<Integer>> graph = new HashMap<>();
	public GiraphFileBuilder readTxtLine(String line) {
		String[] pair = line.split(" ");
		if(!line.startsWith("#") && pair.length==2){
			int origin = Integer.parseInt(pair[0]);
			int destination = Integer.parseInt(pair[1]);
			if(!graph.containsKey(origin)){
				graph.put(origin, new ArrayList<>());
			}
			if(!graph.containsKey(destination)){
				graph.put(destination, new ArrayList<>());
			}
			graph.get(origin).add(destination);

		}
		return this;
	}
	public String toRawString(){
		StringBuilder res = new StringBuilder();
		for (int i : graph.keySet()) {
			StringBuilder node = new StringBuilder("[");
			node.append(i).append(", ").append(-1).append(", [");
			for (int d = 0; d < graph.get(i).size(); d++) {
				if (d != 0) {
					node.append(", ");
				}
				node.append("[").append(d).append(",").append(1.0).append("]");
			}
			node.append("]]\n");
			res.append(node);
		}
		return res.toString();
	}
}
