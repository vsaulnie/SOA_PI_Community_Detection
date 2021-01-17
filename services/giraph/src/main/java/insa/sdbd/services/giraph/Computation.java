package insa.sdbd.services.giraph;

public class Computation {
	private String user = null;
	private String graph = null;
	private String algorithm = null;

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getGraph() {
		return graph;
	}

	public void setGraph(String graph) {
		this.graph = graph;
	}

	public String getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}

	public Computation() {
	}

	@Override
	protected Computation clone() {
		Computation computation = new Computation();
		computation.setUser(this.user);
		computation.setGraph(this.graph);
		computation.setAlgorithm(this.algorithm);
		return computation;
	}
}
