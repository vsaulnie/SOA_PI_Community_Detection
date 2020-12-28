package insa.sdbd.services.giraph.hdprocess;

import insa.sdbd.services.giraph.Computation;
import insa.sdbd.services.giraph.ProcessCommand;

import java.util.LinkedList;
import java.util.List;

public class GiraphRun implements ProcessCommand {
	public static final String SCC_COMPUTATION = "insa.sdbd.community.scc.SCCForwardComputation";
	public static final String SCC_MASTER_COMPUTE = "insa.sdbd.community.scc.SCCMasterComputation";
	public static final String LPA_COMPUTATION ="insa.sdbd.community.lpa.LPAComputation";
	public static final String LPA_MASTER_COMPUTE = "insa.sdbd.community.lpa.LPAMasterCompute";

	public static final String VIF_JLLD = "insa.sdbd.community.formats.vif.JsonLongLongDirectedDouble";
	public static final String VOF_LL = "insa.sdbd.community.formats.vof.VertexWithLongValueTextOutput";

	public static final String GIRAPH_RUNNER = "org.apache.giraph.GiraphRunner";
	public static final String GRAPHARI = "Graphari.jar";

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
	public String giraphRunner = null;
	public String computation = null;
	private boolean isNullOrEmpty(String field){
		return field == null || field.equals("");
	}
	public GiraphRun(){

	}
	public static GiraphRun DefaultGiraphRun(){
		GiraphRun res = new GiraphRun();
		res.jar = GRAPHARI;
		res.giraphRunner = GIRAPH_RUNNER;
		res.w = "1";
		return res;
	}
	public static GiraphRun FromComputation(Computation computationObj,String baseHDFSDir){
		GiraphRun res = DefaultGiraphRun();
		switch(computationObj.getAlgorithm()){
			case "scc":
				res.computation = SCC_COMPUTATION;
				res.mc = SCC_MASTER_COMPUTE;
				res.vif = VIF_JLLD;
				res.vof = VOF_LL;
				break;
			case "lpa":
				res.computation = LPA_COMPUTATION;
				res.mc = LPA_MASTER_COMPUTE;
				res.vif = VIF_JLLD;
				res.vof = VOF_LL;
				break;
			default:
				break;
		}
		res.vip = baseHDFSDir+computationObj.getUser()+"/"+computationObj.getGraph();
		res.op = baseHDFSDir+computationObj.getUser()+"/"+computationObj.getAlgorithm()+"/"+computationObj.getGraph();
		return res;
	}

	public boolean isValid(){
		if(isNullOrEmpty(vif) && isNullOrEmpty(eif))return false;
		if(isNullOrEmpty(vof) && isNullOrEmpty(eof))return false;
		if(isNullOrEmpty(vip) && isNullOrEmpty(eip))return false;
		if(!isNullOrEmpty(vif) && isNullOrEmpty(vip))return false;
		if(!isNullOrEmpty(eif) && isNullOrEmpty(eip))return false;
		if(isNullOrEmpty(giraphRunner))return false;
		if(isNullOrEmpty(jar))return false;
		if(isNullOrEmpty(computation))return false;
		if(isNullOrEmpty(op))return false;
		if(isNullOrEmpty(w)) return false;
		try{
			Integer.parseInt(w);
		}
		catch(Exception e) {
			return false;
		}
		return true;
	}
	public void addOptionIfNotNull(List<String> list, String optionSymbol, String value){
		if(!isNullOrEmpty(value)) {
			list.add("-" + optionSymbol);
			list.add(value);
		}
	}
	public List<String> getCmd(){
		List<String> res = new LinkedList<>();
		res.add("hadoop");
		res.add("jar");
		res.add(jar);
		res.add(giraphRunner);
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
