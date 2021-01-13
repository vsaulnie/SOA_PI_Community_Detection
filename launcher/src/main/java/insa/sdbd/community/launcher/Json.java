package insa.sdbd.community.launcher;

import java.util.ArrayList;

public class Json {
	private ArrayList<String> json = new ArrayList<String>();
	private String deb="{\n";
	private String fin="\n}";
	private String sep=",\n";
	public Json() {
		
	}
	public void add(String key, String value) {
		json.add("\""+key+"\":\""+value+"\"");
	}
	public String toString() {
		String inter="";
		for(String cp : json) {
			inter+="  "+cp+sep;
		}
		inter = inter.substring(0, inter.length() - sep.length());  
		return this.deb+inter+this.fin;
	}

}
