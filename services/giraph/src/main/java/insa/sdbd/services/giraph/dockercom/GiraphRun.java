package insa.sdbd.services.giraph.dockercom;

import java.util.LinkedList;
import java.util.List;

public class GiraphRun implements ProcessCommand {
	public String vif = null;
	public String eif = null;
	public String vip = null;
	public String eip = null;
	public String mc = null;
	public String vof = null;
	public String eof = null;
	public String op = null;
	public String ca = null;
	public String w = null;
	public String jar = null;
	public String computation = null;
	private boolean isNulOrEmpty(String field){
		return field == null || field.equals("");
	}
	public boolean isValid(){
		if(isNulOrEmpty(vif) && isNulOrEmpty(eif))return false;
		if(isNulOrEmpty(vof) && isNulOrEmpty(eof))return false;
		if(isNulOrEmpty(vip) && isNulOrEmpty(eip))return false;
		if(!isNulOrEmpty(vif) && isNulOrEmpty(vip))return false;
		if(!isNulOrEmpty(eif) && isNulOrEmpty(eip))return false;
		if(isNulOrEmpty(jar))return false;
		if(isNulOrEmpty(computation))return false;
		if(isNulOrEmpty(op))return false;
		if(isNulOrEmpty(w)) return false;
		try{
			Integer.parseInt(w);
		}
		catch(Exception e) {
			return false;
		}
		return true;
	}
	public void addOptionIfNotNull(List<String> list, String optionSymbol, String value){
		if(!isNulOrEmpty(value)) {
			list.add("-" + optionSymbol);
			list.add(value);
		}
	}
	public List<String> getCmd(){
		List<String> res = new LinkedList<>();
		res.add("hadoop");
		res.add("jar");
		res.add(jar);
		res.add("org.apache.giraph.GiraphRunner");
		res.add(computation);
		addOptionIfNotNull(res,"mc",mc);
		addOptionIfNotNull(res,"vif",vif);
		addOptionIfNotNull(res,"vip",vip);
		addOptionIfNotNull(res,"eif",eif);
		addOptionIfNotNull(res,"eip",eip);
		addOptionIfNotNull(res,"vof",vof);
		addOptionIfNotNull(res,"eof",eof);
		addOptionIfNotNull(res,"op",op);
		addOptionIfNotNull(res,"ca",ca);
		addOptionIfNotNull(res,"w",w);
		return res;
	}
}
