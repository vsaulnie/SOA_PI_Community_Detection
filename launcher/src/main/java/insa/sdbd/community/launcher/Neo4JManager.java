package insa.sdbd.community.launcher;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Neo4JManager {

	public static String sendQuery(String statment) {
		String body = "{\n" + 
				"  \"statements\" : [ {\n" + 
				"    \"statement\" : \""+statment+"\"\n" + 
				"  } ]\n" + 
				"}";
		
		//Post to StorerMS each query stats : execTime, name, details, Neo4j, input data, query details, return details...
		return null;
	}

	public static String buildQuery(String statment) {
		String body = "{\n" + 
				"  \"statements\" : [ {\n" + 
				"    \"statement\" : \""+statment+"\"\n" + 
				"  } ]\n" + 
				"}";
		
		return body;
	}

}
