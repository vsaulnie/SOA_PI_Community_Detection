package insa.sdbd.services.giraph;

import java.util.HashMap;
import java.util.Map;

public class Results {
	private String status = null;
	private Long execTime = null;
	private String platform = null;
	private String query = null;
	private Map<String, String> infos = new HashMap<>();

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public Results() {
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getExecTime() {
		return execTime;
	}

	public void setExecTime(Long execTime) {
		this.execTime = execTime;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public Map<String, String> getInfos() {
		return infos;
	}

	public void setInfos(Map<String, String> infos) {
		this.infos = infos;
	}
}