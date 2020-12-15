package insa.sdbd.services.giraph;

public class Storage {
	private String userStore;
	private String graphLabel;
	private String vif = null;
	private String eif = null;


	public String getUserStore() {
		return userStore;
	}

	public void setUserStore(String userStore) {
		this.userStore = userStore;
	}

	public String getGraphLabel() {
		return graphLabel;
	}

	public void setGraphLabel(String graphLabel) {
		this.graphLabel = graphLabel;
	}

	public String getVif() {
		return vif;
	}

	public void setVif(String vif) {
		this.vif = vif;
	}

	public String getEif() {
		return eif;
	}

	public void setEif(String eif) {
		this.eif = eif;
	}
}
